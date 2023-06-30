package ru.versoit.todoapp.data.storage.datasources

import kotlinx.coroutines.flow.Flow
import ru.versoit.todoapp.data.models.TodoItemData

interface RemoteTodoItemDataSource {

    companion object {
        const val OK = "ok"
    }
    
    suspend fun getAllTodoItems(): Flow<TodoItemsResponse>

    suspend fun addTodoItem(todoItem: TodoItemData, revision: Int, status: String): TodoItemsResponse

    suspend fun updateTodoItem(todoItem: TodoItemData, revision: Int, status: String): TodoItemResponse

    suspend fun removeTodoItem(id: String, revision: Int): TodoItemResponse

    suspend fun getTodoItemById(id: String): Flow<TodoItemResponse>

    suspend fun updateAllTodoItems(todoItems: List<TodoItemData>, status: String, revision: Int): Int
}

data class TodoItemResponse(val data: TodoItemData?, val lastRevision: Int)

data class TodoItemsResponse(val data: List<TodoItemData>, val lastRevision: Int)