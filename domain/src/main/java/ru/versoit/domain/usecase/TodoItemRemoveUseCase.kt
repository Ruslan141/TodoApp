package ru.versoit.domain.usecase

import ru.versoit.domain.repository.TodoItemRepository

class TodoItemRemoveUseCase(private val todoItemRepository: TodoItemRepository) {

    suspend operator fun invoke(id: String) = todoItemRepository.removeTodoItem(id)
}