package ru.versoit.domain.usecase

import ru.versoit.domain.models.TodoItem
import ru.versoit.domain.repository.TodoItemRepository

class TodoItemUpdateUseCase(private val repository: TodoItemRepository) {

    suspend operator fun invoke(todoItem: TodoItem) = repository.updateTodoItem(todoItem)
}