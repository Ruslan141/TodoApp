package ru.versoit.todoapp.presentation.vmfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.versoit.todoapp.domain.usecase.GetAllTodoItemsUseCase
import ru.versoit.todoapp.domain.usecase.GetTodoItemByIdUseCase
import ru.versoit.todoapp.domain.usecase.TodoItemRemoveUseCase
import ru.versoit.todoapp.domain.usecase.TodoItemUpdateUseCase
import ru.versoit.todoapp.presentation.viewmodels.TodoItemsViewModel

class TodoItemsViewModelFactory(
    private val todoItemUpdateUseCase: TodoItemUpdateUseCase,
    private val todoItemRemoveUseCase: TodoItemRemoveUseCase,
    private val getTodoItemByIdUseCase: GetTodoItemByIdUseCase,
    private val getAllTodoItemsUseCase: GetAllTodoItemsUseCase
) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        TodoItemsViewModel(
            todoItemUpdateUseCase,
            todoItemRemoveUseCase,
            getTodoItemByIdUseCase,
            getAllTodoItemsUseCase
        ) as T
}