package ru.versoit.todoapp.domain.usecase

import ru.versoit.todoapp.domain.repository.TodoItemRepository

class GetTodoItemByIdUseCase(private val todoItemRepository: TodoItemRepository) {

    fun getTodoItemById(id: String) = todoItemRepository.getTodoItemById(id)
}