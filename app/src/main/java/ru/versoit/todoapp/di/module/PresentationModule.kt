package ru.versoit.todoapp.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.versoit.data.storage.datasources.network.NetworkSynchronizer
import ru.versoit.data.storage.datasources.network.SyncCallback
import ru.versoit.domain.usecase.AddTodoItemUseCase
import ru.versoit.domain.usecase.GetAllTodoItemsUseCase
import ru.versoit.domain.usecase.GetTokenUseCase
import ru.versoit.domain.usecase.ManipulateThemesUseCase
import ru.versoit.domain.usecase.NotificationPermissionSelectionUseCase
import ru.versoit.domain.usecase.SaveTokenUseCase
import ru.versoit.domain.usecase.TodoItemRemoveUseCase
import ru.versoit.domain.usecase.TodoItemUpdateUseCase
import ru.versoit.todoapp.presentation.features.notifications.NotificationScheduler
import ru.versoit.todoapp.presentation.features.notifications.NotificationsExecutor
import ru.versoit.todoapp.presentation.features.notifications.TodoNotificationSchedulerImpl
import ru.versoit.todoapp.presentation.features.vmfactory.EditTodoItemViewModelFactory
import ru.versoit.todoapp.presentation.features.vmfactory.MainActivityViewModelFactory
import ru.versoit.todoapp.presentation.features.vmfactory.NewTodoItemViewModelFactory
import ru.versoit.todoapp.presentation.features.vmfactory.TodoItemsViewModelFactory

@Module
class PresentationModule {

    @Provides
    fun provideMainViewModelFactory(
        getTokenUseCase: GetTokenUseCase,
        saveTokenUseCase: SaveTokenUseCase,
        manipulateThemesUseCase: ManipulateThemesUseCase
    ) = MainActivityViewModelFactory(getTokenUseCase, saveTokenUseCase, manipulateThemesUseCase)

    @Provides
    fun provideTodoItemsViewModelFactory(
        todoItemsUpdateUseCase: TodoItemUpdateUseCase,
        todoItemRemoveUseCase: TodoItemRemoveUseCase,
        addTodoItemUseCase: AddTodoItemUseCase,
        getAllTodoItemsUseCase: GetAllTodoItemsUseCase,
        networkSynchronizer: NetworkSynchronizer,
        syncCallback: SyncCallback,
        notificationPermissionSelectionUseCase: NotificationPermissionSelectionUseCase,
        manipulateThemesUseCase: ManipulateThemesUseCase
    ) = TodoItemsViewModelFactory(
        todoItemsUpdateUseCase,
        todoItemRemoveUseCase,
        addTodoItemUseCase,
        getAllTodoItemsUseCase,
        networkSynchronizer,
        syncCallback,
        notificationPermissionSelectionUseCase,
        manipulateThemesUseCase
    )

    @Provides
    fun provideNotificationScheduler(context: Context): NotificationScheduler =
        TodoNotificationSchedulerImpl(context)

    @Provides
    fun provideNewTodoItemViewModelFactory(addTodoItemUseCase: AddTodoItemUseCase) =
        NewTodoItemViewModelFactory(addTodoItemUseCase)

    @Provides
    fun provideEditTodoItemViewModelFactory(
        todoItemsUpdateUseCase: TodoItemUpdateUseCase,
        todoItemsRemoveUseCase: TodoItemRemoveUseCase
    ) = EditTodoItemViewModelFactory(todoItemsUpdateUseCase, todoItemsRemoveUseCase)
}