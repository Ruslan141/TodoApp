package ru.versoit.todoapp.presentation.viewmodels

import ru.versoit.todoapp.domain.models.TodoItem

interface TodoItemRemover {

    fun removeTodoItem(todoItem: TodoItem)
}