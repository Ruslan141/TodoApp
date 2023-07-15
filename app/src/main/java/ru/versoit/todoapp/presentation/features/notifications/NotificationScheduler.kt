package ru.versoit.todoapp.presentation.features.notifications

import ru.versoit.domain.models.TodoItem

interface NotificationScheduler {

    fun scheduleNotifications(todoItems: List<TodoItem>)
}