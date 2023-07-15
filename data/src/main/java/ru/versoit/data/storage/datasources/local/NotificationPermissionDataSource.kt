package ru.versoit.data.storage.datasources.local

interface NotificationPermissionDataSource {

    suspend fun isPermissionSelected(): Boolean

    suspend fun setPermissionSelected()
}