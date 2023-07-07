package ru.versoit.todoapp.data.storage.retrofit.dto

data class TodoItemContainer(
    val status: String,
    val element: TodoItemDTO,
    val revision: Int
)