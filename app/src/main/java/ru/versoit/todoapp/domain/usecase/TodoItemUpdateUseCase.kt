package ru.versoit.todoapp.domain.usecase

import ru.versoit.todoapp.domain.models.TodoItem
import ru.versoit.todoapp.domain.repository.TodoItemRepository

class TodoItemUpdateUseCase(private val repository: TodoItemRepository) {

    fun updateTodoItem(todoItem: TodoItem) = repository.updateTodoItem(todoItem)
}