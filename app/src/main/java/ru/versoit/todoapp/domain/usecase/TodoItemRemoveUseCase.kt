package ru.versoit.todoapp.domain.usecase

import ru.versoit.todoapp.domain.repository.TodoItemRepository

class TodoItemRemoveUseCase(private val todoItemRepository: TodoItemRepository) {

    suspend operator fun invoke(id: String) = todoItemRepository.removeTodoItem(id)
}