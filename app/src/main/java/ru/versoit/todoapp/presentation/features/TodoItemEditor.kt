package ru.versoit.todoapp.presentation.features

import ru.versoit.domain.models.TodoItem

/**
 * Interface to edit todo item.
 */
interface TodoItemEditor {

    fun edit(todoItem: TodoItem)
}