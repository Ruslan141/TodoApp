package ru.versoit.todoapp.domain.usecase

import ru.versoit.todoapp.domain.models.TodoItem
import ru.versoit.todoapp.domain.repository.TodoItemRepository

class TodoItemUpdateUseCase(private val repository: TodoItemRepository) {

    suspend operator fun invoke(todoItem: TodoItem) = repository.updateTodoItem(todoItem)
}