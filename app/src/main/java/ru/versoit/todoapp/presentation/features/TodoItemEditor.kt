package ru.versoit.todoapp.presentation.features

import ru.versoit.todoapp.domain.models.TodoItem

interface TodoItemEditor {

    fun edit(todoItem: TodoItem)
}