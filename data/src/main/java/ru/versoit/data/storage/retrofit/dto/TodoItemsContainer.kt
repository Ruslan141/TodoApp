package ru.versoit.data.storage.retrofit.dto

/**
 * A container that allows to receive and send data about todo items list
 * in a convenient form for the backend.
 *
 * @property status Response status.
 * @property list List of todo items.
 * @property revision The revision number.
 */
data class TodoItemsContainer(
    val status: String,
    val list: List<TodoItemDTO>,
    val revision: Int
)