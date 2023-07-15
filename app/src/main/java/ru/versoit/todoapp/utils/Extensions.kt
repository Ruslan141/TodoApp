package ru.versoit.todoapp.utils

import ru.versoit.domain.models.TodoItem
import ru.versoit.todoapp.presentation.features.TodoItemParcelable


fun TodoItem.toParcelable() = TodoItemParcelable(
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

fun TodoItemParcelable.toDomain() = TodoItem(
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