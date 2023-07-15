package ru.versoit.todoapp.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.versoit.data.repository.NotificationPermissionsRepositoryImpl
import ru.versoit.data.repository.ThemesRepositoryImpl
import ru.versoit.data.repository.TodoItemRepositoryImpl
import ru.versoit.data.repository.TokenRepositoryImpl
import ru.versoit.data.storage.datasources.local.LocalTodoItemDataSource
import ru.versoit.data.storage.datasources.local.NotificationPermissionDataSource
import ru.versoit.data.storage.datasources.local.NotificationPermissionDataSourceImpl
import ru.versoit.data.storage.datasources.local.RoomTodoItemDataSource
import ru.versoit.data.storage.datasources.local.SharedPrefsRevisionDataSource
import ru.versoit.data.storage.datasources.local.ThemeDataSource
import ru.versoit.data.storage.datasources.local.ThemeDataSourceImpl
import ru.versoit.data.storage.datasources.local.TokenDataSource
import ru.versoit.data.storage.datasources.local.TokenDataSourceImpl
import ru.versoit.data.storage.datasources.network.NetworkSynchronizer
import ru.versoit.data.storage.datasources.network.RemoteTodoItemDataSource
import ru.versoit.data.storage.datasources.network.RetrofitTodoItemDataSource
import ru.versoit.data.storage.datasources.network.SyncCallback
import ru.versoit.domain.repository.NotificationPermissionsRepository
import ru.versoit.domain.repository.ThemesRepository
import ru.versoit.domain.repository.TodoItemRepository
import ru.versoit.domain.repository.TokenRepository

@Module
class DataModule {

    @Provides
    fun provideNotificationPermissionsDataSource(context: Context): NotificationPermissionDataSource =
        NotificationPermissionDataSourceImpl(context)

    @Provides
    fun provideThemesDataSource(context: Context): ThemeDataSource = ThemeDataSourceImpl(context)

    @Provides
    fun provideThemesRepository(themeDataSource: ThemeDataSource): ThemesRepository =
        ThemesRepositoryImpl(themeDataSource)

    @Provides
    fun provideNotificationPermissionRepository(notificationPermissionsDataSource: NotificationPermissionDataSource):
            NotificationPermissionsRepository =
        NotificationPermissionsRepositoryImpl(notificationPermissionsDataSource)

    @Provides
    fun provideRemoteTodoItemDataSource(): RemoteTodoItemDataSource = RetrofitTodoItemDataSource()

    @Provides
    fun provideLocalTodoItemDataSource(context: Context): LocalTodoItemDataSource =
        RoomTodoItemDataSource(context)

    @Provides
    fun provideTokenDataSource(context: Context): TokenDataSource =
        TokenDataSourceImpl(context)

    @Provides
    fun provideTodoItemRepository(context: Context): TodoItemRepository = TodoItemRepositoryImpl(
        RoomTodoItemDataSource(context),
        RetrofitTodoItemDataSource(),
        SharedPrefsRevisionDataSource(context),
        TokenDataSourceImpl(context)
    )

    @Provides
    fun provideTodoItemRepositoryImpl(context: Context): TodoItemRepositoryImpl =
        TodoItemRepositoryImpl(
            RoomTodoItemDataSource(context),
            RetrofitTodoItemDataSource(),
            SharedPrefsRevisionDataSource(context),
            TokenDataSourceImpl(context)
        )

    @Provides
    fun provideTokenRepository(tokenDataSource: TokenDataSource): TokenRepository =
        TokenRepositoryImpl(tokenDataSource)

    @Provides
    fun bindNetworkSynchronizer(todoItemRepository: TodoItemRepositoryImpl): NetworkSynchronizer =
        todoItemRepository

    @Provides
    fun bindSyncCallback(todoItemRepository: TodoItemRepositoryImpl): SyncCallback =
        todoItemRepository

}