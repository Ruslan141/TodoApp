package ru.versoit.data.storage.retrofit.dto

/**
 * A container that allows to receive and send data about single todo item
 * in a convenient form for the backend.
 */
data class TodoItemContainer(
    val status: String,
    val element: TodoItemDTO,
    val revision: Int
)