package ru.versoit.todoapp.presentation.features.notifications

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import ru.versoit.domain.models.TodoItem
import ru.versoit.domain.models.isExpired
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

class TodoNotificationSchedulerImpl(private val context: Context) : NotificationScheduler {

    override fun scheduleNotifications(todoItems: List<TodoItem>) {
        todoItems.forEach { todoItem ->
            todoItem.deadline?.let {
                scheduleNotificationWorker(todoItem)
            }
        }
    }

    private fun scheduleNotificationWorker(todoItem: TodoItem) {
        if (!todoItem.isExpired()) {

            val notificationTime = getNotificationTime(todoItem.deadline!!)
            val data = Data.Builder()
                .putString(NotificationsExecutor.TODO_ID, todoItem.id)
                .putLong(NotificationsExecutor.TODO_DEADLINE, todoItem.deadline!!.time)
                .build()

            val notificationDelay = notificationTime - System.currentTimeMillis()
            if (notificationDelay > 0) {
                val workRequest = OneTimeWorkRequestBuilder<NotificationsExecutor>()
                    .setInputData(data)
                    .setInitialDelay(notificationDelay, TimeUnit.MILLISECONDS)
                    .build()

                WorkManager.getInstance(context).enqueueUniqueWork(
                    todoItem.id,
                    ExistingWorkPolicy.REPLACE,
                    workRequest
                )
            }
        }
    }

    private fun getNotificationTime(deadline: Date): Long {
        val calendar = Calendar.getInstance()
        calendar.time = deadline

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        return calendar.time.time
    }
}