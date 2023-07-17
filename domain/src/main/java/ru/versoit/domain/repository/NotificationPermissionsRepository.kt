package ru.versoit.domain.repository

interface NotificationPermissionsRepository {

    suspend fun isPermissionSelected(): Boolean

    suspend fun setPermissionSelected()
}