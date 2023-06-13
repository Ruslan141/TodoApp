package ru.versoit.todoapp.domain.models

import java.util.Date

enum class Importance {
    UNIMPORTANT,
    LESS_IMPORTANT,
    IMPORTANT
}

data class TodoItem(
    val id: String,
    var text: String,
    var importance: Importance,
    var deadline: Date,
    var state: Boolean,
    var dateCreate: Date,
    var dateChange: Date
)