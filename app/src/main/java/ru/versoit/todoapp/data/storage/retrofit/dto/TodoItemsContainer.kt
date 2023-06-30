package ru.versoit.todoapp.data.storage.retrofit.dto

data class TodoItemsContainer(
    val status: String,
    val list: List<TodoItemDTO>,
    val revision: Int
)