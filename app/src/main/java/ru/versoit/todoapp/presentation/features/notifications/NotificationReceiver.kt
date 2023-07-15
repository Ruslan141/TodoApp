package ru.versoit.todoapp.presentation.features.notifications

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ru.versoit.domain.models.TodoItem
import ru.versoit.todoapp.R

class NotificationReceiver : BroadcastReceiver() {

    companion object {
        const val DEADLINE_EXTRA_NAME = "DEADLINE"
        const val TODO_NAME_EXTRA_NAME = "TODO_NAME"
        const val TODO_NOTIFICATION_CHANNEL_NAME = "TODO_NOTIFICATION_CHANNEL"
        const val TODO_ID_EXTRA_NAME = "TODO_ID"
    }

    var todoItems: List<TodoItem>? = null

    override fun onReceive(context: Context, intent: Intent) {

        val text = intent.getStringExtra(DEADLINE_EXTRA_NAME)
        val title = intent.getStringExtra(TODO_NAME_EXTRA_NAME)
        val id = intent.getIntExtra(TODO_ID_EXTRA_NAME, 0)

        if (title != null && text != null && todoItems != null) {
            if (todoItems!!.any { it.id.hashCode() == id }) {
                val notificationBuilder = getNotificationBuilder(title, text, context)

                val notificationManager = NotificationManagerCompat.from(context)

                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }

                notificationManager.notify(id, notificationBuilder.build())
            }
        }
    }

    private fun getNotificationBuilder(title: String, text: String, context: Context) =
        NotificationCompat.Builder(context, TODO_NOTIFICATION_CHANNEL_NAME)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
}
