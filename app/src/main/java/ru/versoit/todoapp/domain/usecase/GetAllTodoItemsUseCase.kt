package ru.versoit.todoapp.domain.usecase

import ru.versoit.todoapp.domain.repository.TodoItemRepository

class GetAllTodoItemsUseCase(private val todoItemRepository: TodoItemRepository) {

    suspend operator fun invoke() = todoItemRepository.getAllTodoItems()
}