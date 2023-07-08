package ru.versoit.todoapp.presentation.viewmodels

import ru.versoit.domain.models.TodoItem

/**
 * Interface for removing todo item.
 */

interface TodoItemRemover {

    fun removeTodoItem(todoItem: TodoItem)
}