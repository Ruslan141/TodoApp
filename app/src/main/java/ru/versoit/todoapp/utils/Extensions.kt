package ru.versoit.todoapp.utils

import ru.versoit.todoapp.data.models.TodoItemData
import ru.versoit.todoapp.domain.models.TodoItem
import java.util.Date

const val DATE_FORMAT = "d MMM yyyy"

fun Long.toDate() = Date(this)

fun TodoItem.toData() = TodoItemData(
    id = id,
    text = text,
    importance = importance,
    deadline = deadline,
    done = done,
    color = color,
    created = created,
    lastUpdate = lastUpdate,
    lastUpdatedBy = lastUpdatedBy,
)