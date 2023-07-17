package ru.versoit.domain.usecase

import ru.versoit.domain.repository.NotificationPermissionsRepository

class NotificationPermissionSelectionUseCase(private val repository: NotificationPermissionsRepository) {

    suspend fun isPermissionSelected(): Boolean = repository.isPermissionSelected()

    suspend fun setPermissionSelected() = repository.setPermissionSelected()
}