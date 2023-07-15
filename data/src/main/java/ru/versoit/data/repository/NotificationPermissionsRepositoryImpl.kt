package ru.versoit.data.repository

import ru.versoit.data.storage.datasources.local.NotificationPermissionDataSource
import ru.versoit.domain.repository.NotificationPermissionsRepository

class NotificationPermissionsRepositoryImpl(
    private val notificationPermissionDataSource: NotificationPermissionDataSource
) :
    NotificationPermissionsRepository {
    override suspend fun isPermissionSelected() =
        notificationPermissionDataSource.isPermissionSelected()

    override suspend fun setPermissionSelected() =
        notificationPermissionDataSource.setPermissionSelected()
}