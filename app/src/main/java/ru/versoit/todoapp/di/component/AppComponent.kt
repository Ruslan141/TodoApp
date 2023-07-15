package ru.versoit.todoapp.di.component

import dagger.Component
import ru.versoit.todoapp.di.scopes.AppScope
import ru.versoit.todoapp.di.module.AppModule

@AppScope
@Component(
    modules = [AppModule::class]
)
interface AppComponent {

    fun mainActivityComponent(): MainActivityComponent
    fun todoItemsFragmentComponent(): TodoItemsFragmentComponent
    fun newTodoItemFragmentComponent(): NewTodoItemFragmentComponent
    fun editTodoItemFragmentComponent(): EditTodoItemFragmentComponent
    fun notificationsExecutorComponent(): NotificationsExecutorComponent
}