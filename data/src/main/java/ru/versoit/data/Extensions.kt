package ru.versoit.data

import ru.versoit.data.models.TodoItemData
import ru.versoit.domain.models.TodoItem
import java.util.Date

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