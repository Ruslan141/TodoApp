package ru.versoit.todoapp.domain.usecase

import ru.versoit.todoapp.domain.repository.TodoItemRepository

class TodoItemRemoveUseCase(private val todoItemRepository: TodoItemRepository) {

    fun removeTodoItem(id: String) = todoItemRepository.removeTodoItem(id)
}