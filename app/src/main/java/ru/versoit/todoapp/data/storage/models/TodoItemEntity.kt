package ru.versoit.todoapp.data.storage.models

import ru.versoit.todoapp.domain.models.TodoItem
import ru.versoit.todoapp.domain.models.Importance
import java.util.Date

data class TodoItemEntity(
    var id: String,
    val text: String,
    val importance: Importance,
    val deadline: Date,
    val state: Boolean,
    val isDeadline: Boolean,
    val dateCreate: Date,
    val dateChange: Date
) {
    fun toDomain(): TodoItem {
        return TodoItem(
            id,
            text,
            importance,
            deadline,
            state,
            isDeadline,
            dateCreate,
            dateChange
        )
    }
}