package ru.versoit.todoapp.presentation.fragments

interface NotificationPermissionsSelector {

    suspend fun isNotificationPermissionSelected(): Boolean

    suspend fun setNotificationPermissionSelected()
}