package ru.versoit.todoapp.data.storage.datasources

import kotlinx.coroutines.flow.Flow
import ru.versoit.todoapp.data.models.TodoItemData

interface LocalTodoItemDataSource {

    suspend fun getAllTodoItems(): Flow<List<TodoItemData>>

    suspend fun addTodoItem(todoItem: TodoItemData)

    suspend fun updateTodoItem(todoItem: TodoItemData)

    suspend fun removeTodoItem(id: String)

    suspend fun getTodoItemById(id: String): Flow<TodoItemData?>

    suspend fun insertAllTodoItems(todoItems: List<TodoItemData>)
}