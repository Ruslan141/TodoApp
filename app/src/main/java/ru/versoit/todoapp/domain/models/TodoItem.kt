package ru.versoit.todoapp.domain.models

import java.io.Serializable
import java.util.Date

enum class Importance {
    UNIMPORTANT,
    LESS_IMPORTANT,
    IMPORTANT
}

data class TodoItem(
    var id: String,
    val text: String,
    val importance: Importance,
    val deadline: Date?,
    val done: Boolean,
    val color: String?,
    val created: Date,
    val lastUpdate: Date,
    val lastUpdatedBy: String
) : Serializable