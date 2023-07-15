package ru.versoit.todoapp.presentation.features.vmfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.versoit.data.storage.datasources.network.NetworkSynchronizer
import ru.versoit.data.storage.datasources.network.SyncCallback
import ru.versoit.domain.usecase.AddTodoItemUseCase
import ru.versoit.domain.usecase.GetAllTodoItemsUseCase
import ru.versoit.domain.usecase.ManipulateThemesUseCase
import ru.versoit.domain.usecase.NotificationPermissionSelectionUseCase
import ru.versoit.domain.usecase.TodoItemRemoveUseCase
import ru.versoit.domain.usecase.TodoItemUpdateUseCase
import ru.versoit.todoapp.presentation.viewmodels.TodoItemsViewModel

class TodoItemsViewModelFactory(
    private val todoItemUpdateUseCase: TodoItemUpdateUseCase,
    private val todoItemRemoveUseCase: TodoItemRemoveUseCase,
    private val addTodoItemUseCase: AddTodoItemUseCase,
    private val getAllTodoItemsUseCase: GetAllTodoItemsUseCase,
    private val networkSynchronizer: NetworkSynchronizer,
    private val syncCallback: SyncCallback,
    private val notificationPermissionSelectionUseCase: NotificationPermissionSelectionUseCase,
    private val manipulateThemesUseCase: ManipulateThemesUseCase
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
            syncCallback,
            notificationPermissionSelectionUseCase,
            manipulateThemesUseCase
        ) as T
}