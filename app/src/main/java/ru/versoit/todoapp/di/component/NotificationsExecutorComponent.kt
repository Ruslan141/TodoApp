package ru.versoit.todoapp.di.component

import dagger.Subcomponent
import ru.versoit.todoapp.di.module.DataModule
import ru.versoit.todoapp.di.module.DomainModule
import ru.versoit.todoapp.di.module.PresentationModule
import ru.versoit.todoapp.di.scopes.WorkerScope
import ru.versoit.todoapp.presentation.features.notifications.NotificationsExecutor

@WorkerScope
@Subcomponent(modules = [DomainModule::class, PresentationModule::class, DataModule::class])
interface NotificationsExecutorComponent {

    fun inject(notificationsExecutor: NotificationsExecutor)
}