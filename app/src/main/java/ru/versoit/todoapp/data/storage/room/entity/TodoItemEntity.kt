package ru.versoit.todoapp.data.storage.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.versoit.todoapp.data.models.TodoItemData
import ru.versoit.todoapp.domain.models.Importance
import java.util.Date

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