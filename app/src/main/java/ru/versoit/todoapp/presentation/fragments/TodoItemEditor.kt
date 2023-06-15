package ru.versoit.todoapp.presentation.fragments

import ru.versoit.todoapp.domain.models.TodoItem

interface TodoItemEditor {

    fun edit(todoItem: TodoItem)
}