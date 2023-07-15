package ru.versoit.data.storage.datasources.local

import android.content.Context

class NotificationPermissionDataSourceImpl(context: Context) : NotificationPermissionDataSource {

    companion object {
        private const val IS_NOTIFICATION_PERMISSION_SELECTED_SHARED_PREFS =
            "IS_NOTIFICATION_PERMISSION_SELECTED_SHARED_PREFS"
        private const val IS_NOTIFICATION_SELECTED_NAME = "IS_NOTIFICATION_SELECTED_NAME"
    }

    private val sharedPrefs = context.getSharedPreferences(
        IS_NOTIFICATION_PERMISSION_SELECTED_SHARED_PREFS,
        Context.MODE_PRIVATE
    )

    override suspend fun isPermissionSelected() =
        sharedPrefs.getBoolean(IS_NOTIFICATION_SELECTED_NAME, false)

    override suspend fun setPermissionSelected() =
        sharedPrefs.edit().putBoolean(IS_NOTIFICATION_SELECTED_NAME, true).apply()

}