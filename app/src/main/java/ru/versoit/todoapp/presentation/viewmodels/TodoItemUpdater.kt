package ru.versoit.todoapp.presentation.viewmodels

import ru.versoit.todoapp.domain.models.TodoItem

interface TodoItemUpdater {

    fun updateTodoItem(todoItem: TodoItem)
}