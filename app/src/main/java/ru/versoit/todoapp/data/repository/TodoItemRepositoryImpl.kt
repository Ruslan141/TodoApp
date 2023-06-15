package ru.versoit.todoapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.versoit.todoapp.data.storage.datasources.TodoItemDataSource
import ru.versoit.todoapp.data.storage.models.TodoItemEntity
import ru.versoit.todoapp.domain.models.TodoItem
import ru.versoit.todoapp.domain.repository.TodoItemRepository

class TodoItemRepositoryImpl(private val todoItemDataSource: TodoItemDataSource) : TodoItemRepository {

    override fun getAllTodoItems(): Flow<List<TodoItem>> {
        return todoItemDataSource.getAllTodoItems().map { it -> it.map { it.toDomain() } }
    }

    override fun addTodoItem(todoItem: TodoItem) {
        todoItemDataSource.addTodoItem(mapToData(todoItem))
    }

    override fun updateTodoItem(todoItem: TodoItem) {
        todoItemDataSource.updateTodoItem(mapToData(todoItem))
    }

    override fun removeTodoItem(id: String) {
        todoItemDataSource.removeTodoItem(id)
    }

    override fun getTodoItemById(id: String): Flow<TodoItem?> {
        return todoItemDataSource.getTodoItemById(id).map { it?.toDomain() }
    }

    private fun mapToData(todoItem: TodoItem) = TodoItemEntity(
        todoItem.id,
        todoItem.text,
        todoItem.importance,
        todoItem.deadline,
        todoItem.completed,
        todoItem.isDeadline,
        todoItem.dateCreate,
        todoItem.lastChanged
    )
}