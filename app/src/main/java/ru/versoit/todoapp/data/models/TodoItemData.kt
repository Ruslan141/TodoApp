package ru.versoit.todoapp.data.models

import ru.versoit.todoapp.data.storage.room.entity.TodoItemEntity
import ru.versoit.todoapp.domain.models.Importance
import ru.versoit.todoapp.domain.models.TodoItem
import java.util.Date

data class TodoItemData(

    val id: String,
    val text: String,
    val importance: Importance,
    val deadline: Date?,
    val done: Boolean,
    val color: String?,
    val created: Date,
    val lastUpdate: Date,
    val lastUpdatedBy: String
)

fun TodoItemData.toDomain() = TodoItem(
    id = id,
    text = text,
    importance = importance,
    deadline = deadline,
    done = done,
    created = created,
    color = color,
    lastUpdate = lastUpdate,
    lastUpdatedBy = lastUpdatedBy
)

fun TodoItemData.toLocal() = TodoItemEntity(
    id = id,
    text = text,
    importance = importance,
    deadline = deadline,
    done = done,
    created = created,
    color = color,
    lastUpdate = lastUpdate,
    lastUpdatedBy = lastUpdatedBy
)

fun importanceToRemote(importance: Importance): String =
    when (importance) {
        Importance.IMPORTANT -> "important"
        Importance.LESS_IMPORTANT -> "basic"
        Importance.UNIMPORTANT -> "low"
    }