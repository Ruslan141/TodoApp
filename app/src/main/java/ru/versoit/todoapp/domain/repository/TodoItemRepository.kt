package ru.versoit.todoapp.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.versoit.todoapp.domain.models.TodoItem

interface TodoItemRepository {

    fun getAllTodoItems(): Flow<List<TodoItem>>

    fun addTodoItem(todoItem: TodoItem)

    fun updateTodoItem(todoItem: TodoItem)

    fun removeTodoItem(id: String)

    fun getTodoItemById(id: String): Flow<TodoItem?>
}