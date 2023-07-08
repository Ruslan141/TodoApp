package ru.versoit.todoapp.di.module

import dagger.Module
import dagger.Provides
import ru.versoit.data.storage.datasources.network.NetworkSynchronizer
import ru.versoit.data.storage.datasources.network.SyncCallback
import ru.versoit.domain.usecase.AddTodoItemUseCase
import ru.versoit.domain.usecase.GetAllTodoItemsUseCase
import ru.versoit.domain.usecase.GetTokenUseCase
import ru.versoit.domain.usecase.SaveTokenUseCase
import ru.versoit.domain.usecase.TodoItemRemoveUseCase
import ru.versoit.domain.usecase.TodoItemUpdateUseCase
import ru.versoit.todoapp.presentation.features.vmfactory.EditTodoItemViewModelFactory
import ru.versoit.todoapp.presentation.features.vmfactory.MainActivityViewModelFactory
import ru.versoit.todoapp.presentation.features.vmfactory.NewTodoItemViewModelFactory
import ru.versoit.todoapp.presentation.features.vmfactory.TodoItemsViewModelFactory

@Module
class PresentationModule {

    @Provides
    fun provideMainViewModelFactory(
        getTokenUseCase: GetTokenUseCase,
        saveTokenUseCase: SaveTokenUseCase
    ) = MainActivityViewModelFactory(getTokenUseCase, saveTokenUseCase)

    @Provides
    fun provideTodoItemsViewModelFactory(
        todoItemsUpdateUseCase: TodoItemUpdateUseCase,
        todoItemRemoveUseCase: TodoItemRemoveUseCase,
        addTodoItemUseCase: AddTodoItemUseCase,
        getAllTodoItemsUseCase: GetAllTodoItemsUseCase,
        networkSynchronizer: NetworkSynchronizer,
        syncCallback: SyncCallback
    ) = TodoItemsViewModelFactory(
        todoItemsUpdateUseCase,
        todoItemRemoveUseCase,
        addTodoItemUseCase,
        getAllTodoItemsUseCase,
        networkSynchronizer,
        syncCallback
    )

    @Provides
    fun provideNewTodoItemViewModelFactory(addTodoItemUseCase: AddTodoItemUseCase) =
        NewTodoItemViewModelFactory(addTodoItemUseCase)

    @Provides
    fun provideEditTodoItemViewModelFactory(
        todoItemsUpdateUseCase: TodoItemUpdateUseCase,
        todoItemsRemoveUseCase: TodoItemRemoveUseCase
    ) = EditTodoItemViewModelFactory(todoItemsUpdateUseCase, todoItemsRemoveUseCase)
}