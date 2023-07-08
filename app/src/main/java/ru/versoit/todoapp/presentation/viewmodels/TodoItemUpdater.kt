package ru.versoit.todoapp.presentation.viewmodels

import ru.versoit.domain.models.TodoItem

/**
 * Interface for updating todoItem.
 */

interface TodoItemUpdater {

    fun updateTodoItem(todoItem: ru.versoit.domain.models.TodoItem)
}