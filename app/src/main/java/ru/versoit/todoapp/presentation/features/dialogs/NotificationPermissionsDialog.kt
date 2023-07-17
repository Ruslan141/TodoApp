package ru.versoit.todoapp.presentation.features.dialogs

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.LayoutInflater
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.versoit.todoapp.R
import ru.versoit.todoapp.databinding.DialogNotificationPermissionBinding
import ru.versoit.todoapp.presentation.fragments.NotificationPermissionsSelector

class NotificationPermissionsDialog(
    private val context: Context,
    private val notificationPermissionsSelector: NotificationPermissionsSelector,
    layoutInflater: LayoutInflater,
) {

    private val dialogNotificationBinding =
        DialogNotificationPermissionBinding.inflate(layoutInflater)

    suspend fun tryShow() {
        if (!notificationPermissionsSelector.isNotificationPermissionSelected()) {
            val dialog = AlertDialog.Builder(context, R.style.AlertDialog)
                .setView(dialogNotificationBinding.root)
                .setCancelable(false)
                .create()

            initEvents(dialog)
            dialog.show()
        }
    }

    private fun requestNotificationPermission() {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        val packageName = context.packageName
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        context.startActivity(intent)
    }

    private fun initEvents(dialog: AlertDialog) {
        dialogNotificationBinding.buttonAllow.setOnClickListener {
            setPermissionSelected()
            requestNotificationPermission()
            dialog.dismiss()
        }

        dialogNotificationBinding.buttonDoNotAllow.setOnClickListener {
            setPermissionSelected()
            requestNotificationPermission()
            dialog.dismiss()
        }
    }

    private fun setPermissionSelected() {
        CoroutineScope(Dispatchers.Default).launch {
            notificationPermissionsSelector.setNotificationPermissionSelected()
        }
    }
}