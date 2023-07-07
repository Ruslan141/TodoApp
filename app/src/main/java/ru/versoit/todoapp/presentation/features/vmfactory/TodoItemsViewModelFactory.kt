package ru.versoit.todoapp.presentation.features.vmfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.versoit.todoapp.domain.repository.NetworkSynchronizer
import ru.versoit.todoapp.domain.repository.SyncCallback
import ru.versoit.todoapp.domain.usecase.AddTodoItemUseCase
import ru.versoit.todoapp.domain.usecase.GetAllTodoItemsUseCase
import ru.versoit.todoapp.domain.usecase.TodoItemRemoveUseCase
import ru.versoit.todoapp.domain.usecase.TodoItemUpdateUseCase
import ru.versoit.todoapp.presentation.viewmodels.TodoItemsViewModel

class TodoItemsViewModelFactory(
    private val todoItemUpdateUseCase: TodoItemUpdateUseCase,
    private val todoItemRemoveUseCase: TodoItemRemoveUseCase,
    private val addTodoItemUseCase: AddTodoItemUseCase,
    private val getAllTodoItemsUseCase: GetAllTodoItemsUseCase,
    private val networkSynchronizer: NetworkSynchronizer,
    private val syncCallback: SyncCallback,
) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        TodoItemsViewModel(
            todoItemUpdateUseCase,
            todoItemRemoveUseCase,
            addTodoItemUseCase,
            getAllTodoItemsUseCase,
            networkSynchronizer,
            syncCallback
        ) as T
}