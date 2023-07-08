package ru.versoit.todoapp.di.module

import dagger.Module
import dagger.Provides
import ru.versoit.domain.repository.TodoItemRepository
import ru.versoit.domain.repository.TokenRepository
import ru.versoit.domain.usecase.AddTodoItemUseCase
import ru.versoit.domain.usecase.GetAllTodoItemsUseCase
import ru.versoit.domain.usecase.GetTokenUseCase
import ru.versoit.domain.usecase.SaveTokenUseCase
import ru.versoit.domain.usecase.TodoItemRemoveUseCase
import ru.versoit.domain.usecase.TodoItemUpdateUseCase

@Module
class DomainModule {

    @Provides
    fun provideAddTodoItemUseCase(todoItemRepository: TodoItemRepository) =
        AddTodoItemUseCase(todoItemRepository = todoItemRepository)

    @Provides
    fun provideGetAllTodoItemsUseCase(todoItemRepository: TodoItemRepository) =
        GetAllTodoItemsUseCase(todoItemRepository = todoItemRepository)

    @Provides
    fun provideGetTokenUseCase(tokenRepository: TokenRepository) =
        GetTokenUseCase(tokenRepository = tokenRepository)

    @Provides
    fun provideSaveTokenUseCase(tokenRepository: TokenRepository) =
        SaveTokenUseCase(tokenRepository = tokenRepository)

    @Provides
    fun provideTodoItemRemoveUseCase(todoItemRepository: TodoItemRepository) =
        TodoItemRemoveUseCase(todoItemRepository = todoItemRepository)

    @Provides
    fun provideTodoItemUpdateUseCase(todoItemRepository: TodoItemRepository) =
        TodoItemUpdateUseCase(repository = todoItemRepository)
}