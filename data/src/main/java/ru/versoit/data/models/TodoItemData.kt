package ru.versoit.data.models

import ru.versoit.data.storage.room.entity.TodoItemEntity
import ru.versoit.domain.models.Importance
import ru.versoit.domain.models.TodoItem
import java.util.Date

/**
 * A todo item class in data-layer.
 *
 * @property id The id of the TodoItem.
 * @property text The text description of the TodoItem.
 * @property importance The importance level of the TodoItem.
 * @property deadline The deadline of the TodoItem, nullable.
 * @property done Indicates whether the TodoItem is marked as done.
 * @property color The color associated with the TodoItem, nullable.
 * @property created The timestamp of when the TodoItem was created.
 * @property lastUpdate The timestamp of the last update to the TodoItem.
 * @property lastUpdatedBy The identifier of the user who last updated the TodoItem.
 */

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

fun ru.versoit.data.models.TodoItemData.toDomain() = TodoItem(
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

/**
 * Converts a object of class TodoItemData a to convenient object for a local data source.
 */
fun ru.versoit.data.models.TodoItemData.toLocal() = TodoItemEntity(
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

/**
 * Converts a object of class TodoItemData a to convenient object for a remote data source.
 */
fun importanceToRemote(importance: Importance): String =
    when (importance) {
        Importance.IMPORTANT -> "important"
        Importance.LESS_IMPORTANT -> "basic"
        Importance.UNIMPORTANT -> "low"
    }