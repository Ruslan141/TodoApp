package ru.versoit.todoapp.domain.models

import java.io.Serializable
import java.util.Date

enum class Importance {
    UNIMPORTANT,
    LESS_IMPORTANT,
    IMPORTANT
}

data class TodoItem(
    val id: String,
    val text: String,
    val importance: Importance,
    val deadline: Date,
    val completed: Boolean,
    val isDeadline: Boolean,
    val dateCreate: Date,
    val lastChanged: Date
) : Serializable