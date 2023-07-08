package ru.versoit.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.versoit.domain.models.TodoItem

interface TodoItemRepository {

    suspend fun getAllTodoItems(): Flow<List<TodoItem>>

    suspend fun addTodoItem(todoItem: TodoItem)

    suspend fun updateTodoItem(todoItem: TodoItem)

    suspend fun removeTodoItem(id: String)
}