package ru.versoit.todoapp.domain.usecase

import ru.versoit.todoapp.domain.models.TodoItem
import ru.versoit.todoapp.domain.repository.TodoItemRepository

class AddTodoItemUseCase(private val todoItemRepository: TodoItemRepository) {

    fun addTodoItem(todoItem: TodoItem) = todoItemRepository.addTodoItem(todoItem)
}