package ru.versoit.todoapp.data.storage.datasources

import kotlinx.coroutines.flow.Flow
import ru.versoit.todoapp.data.storage.models.TodoItemEntity

interface TodoItemDataSource {

    fun getAllTodoItems(): Flow<List<TodoItemEntity>>

    fun addTodoItem(todoItem: TodoItemEntity)

    fun updateTodoItem(todoItem: TodoItemEntity)

    fun removeTodoItem(id: String)

    fun getTodoItemById(id: String): Flow<TodoItemEntity?>
}