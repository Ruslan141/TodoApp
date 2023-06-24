package ru.versoit.todoapp.presentation.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.versoit.todoapp.domain.usecase.AddTodoItemUseCase
import ru.versoit.todoapp.domain.usecase.GetAllTodoItemsUseCase
import ru.versoit.todoapp.domain.usecase.TodoItemRemoveUseCase
import ru.versoit.todoapp.domain.usecase.TodoItemUpdateUseCase
import ru.versoit.todoapp.presentation.viewmodels.TodoItemsViewModel

class TodoItemsViewModelFactory(
    private val todoItemUpdateUseCase: TodoItemUpdateUseCase,
    private val todoItemRemoveUseCase: TodoItemRemoveUseCase,
    private val addTodoItemUseCase: AddTodoItemUseCase,
    private val getAllTodoItemsUseCase: GetAllTodoItemsUseCase
) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        TodoItemsViewModel(
            todoItemUpdateUseCase,
            todoItemRemoveUseCase,
            addTodoItemUseCase,
            getAllTodoItemsUseCase
        ) as T
}