package ru.versoit.todoapp.data.storage.models

import ru.versoit.todoapp.domain.models.TodoItem
import ru.versoit.todoapp.domain.models.Importance
import java.util.Date

enum class Importance {
    Unimportant,
    LessImportant,
    Important
}

data class TodoItemEntity(
    val id: String,
    var text: String,
    var importance: Importance,
    var deadline: Date,
    var execution: Boolean,
    var dateCreate: Date,
    var dateChange: Date
) {
    fun toDomain(): TodoItem {
        return TodoItem(
            id,
            text,
            importance,
            deadline,
            execution,
            dateCreate,
            dateChange
        )
    }
}