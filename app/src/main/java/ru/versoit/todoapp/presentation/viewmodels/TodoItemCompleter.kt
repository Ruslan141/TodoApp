package ru.versoit.todoapp.presentation.viewmodels

import ru.versoit.domain.models.TodoItem

interface TodoItemCompleter {

    fun setCompletedTodoItem(todoItem: TodoItem)
}