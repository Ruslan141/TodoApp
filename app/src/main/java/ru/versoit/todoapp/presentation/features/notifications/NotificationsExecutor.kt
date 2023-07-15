package ru.versoit.todoapp.presentation.features.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.flow.first
import ru.versoit.domain.models.TodoItem
import ru.versoit.domain.usecase.GetAllTodoItemsUseCase
import ru.versoit.todoapp.R
import ru.versoit.todoapp.app.TodoApp
import ru.versoit.todoapp.presentation.MainActivity
import ru.versoit.todoapp.utils.toParcelable
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class NotificationsExecutor(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        const val TODO_DEADLINE = "TODO_NAME"
        const val TODO_ID = "TODO_ID"

        private const val TODO_NOTIFICATION_CHANNEL_ID = "TODO_NOTIFICATION_CHANNEL"
        private const val NOTIFICATION_DATE_FORMAT_PATTERN = "dd MMMM"
    }

    @Inject
    lateinit var getAllTodoItemsUseCase: GetAllTodoItemsUseCase

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        inject()
    }

    override suspend fun doWork(): Result {
        val deadline = inputData.getLong(TODO_DEADLINE, 0)
        val id = inputData.getString(TODO_ID)

        val todoItemFromRepository = getTodoItemFromRepository(id, deadline)
        val content = String.format(
            Locale.getDefault(),
            "%s %s",
            formattedDeadline(deadline),
            applicationContext.getString(R.string.deadline_will_expire),
        ).lowercase()
        if (todoItemFromRepository != null && !todoItemFromRepository.done) {
            createNotification(
                todoItemFromRepository.text,
                content,
                id.hashCode(),
                todoItemFromRepository
            )
        }

        return Result.success()
    }

    private fun inject() {
        (applicationContext as TodoApp).appComponent.notificationsExecutorComponent().inject(this)
    }

    private suspend fun getTodoItemFromRepository(
        todoItemId: String?,
        todoItemDeadline: Long
    ): TodoItem? {

        val todoItems = getAllTodoItemsUseCase().first()

        return todoItems.find { todoItem -> todoItem.id == todoItemId && todoItem.deadline!!.time == todoItemDeadline }
    }

    private fun formattedDeadline(deadline: Long) =
        SimpleDateFormat(NOTIFICATION_DATE_FORMAT_PATTERN, Locale.getDefault()).format(deadline)

    private fun createNotification(title: String?, message: String?, id: Int, todoItem: TodoItem) {
        createNotificationChannel()

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            putExtra(MainActivity.TODO_ITEM_PARCELABLE_NAME, todoItem.toParcelable())
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification =
            NotificationCompat.Builder(applicationContext, TODO_NOTIFICATION_CHANNEL_ID)
                .setContentText(message)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

        notificationManager.notify(id, notification)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            TODO_NOTIFICATION_CHANNEL_ID,
            applicationContext.getString(R.string.base_notifications),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }


}