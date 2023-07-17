package ru.versoit.domain.usecase

import ru.versoit.domain.repository.TodoItemRepository

class GetAllTodoItemsUseCase(private val todoItemRepository: TodoItemRepository) {

    suspend operator fun invoke() = todoItemRepository.getAllTodoItems()
}