package ru.versoit.todoapp.domain.usecase

import ru.versoit.todoapp.domain.models.TodoItem
import ru.versoit.todoapp.domain.repository.TodoItemRepository
import java.util.UUID

class AddTodoItemUseCase(private val todoItemRepository: TodoItemRepository) {

    suspend operator fun invoke(todoItem: TodoItem) =
        todoItemRepository.addTodoItem(todoItem.copy(id = UUID.randomUUID().toString()))
}