package ru.versoit.data.storage.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.versoit.data.models.TodoItemData
import ru.versoit.domain.models.Importance
import java.util.Date

/**
 * Represents a todo item stored in a database as entity.
 *
 * @property id The unique identifier of the TodoItem.
 * @property text The text content of the TodoItem.
 * @property importance The importance level of the TodoItem.
 * @property deadline The deadline of the TodoItem (nullable).
 * @property done Indicates whether the TodoItem is marked as done.
 * @property color The color associated with the TodoItem (nullable).
 * @property created The date when the TodoItem was created.
 * @property lastUpdate The date of the last update to the TodoItem.
 * @property lastUpdatedBy The identifier of the entity that last updated the TodoItem.
 */
@Entity(tableName = "todoItems")
data class TodoItemEntity(

    @PrimaryKey
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

/**
 * Converts TodoItemEntity to its corresponding data representation.
 *
 * @return TodoItemData object representing the TodoItemEntity.
 */
fun TodoItemEntity.toData() = TodoItemData(
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