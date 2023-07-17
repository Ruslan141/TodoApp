package ru.versoit.todoapp.di.component

import dagger.Subcomponent
import ru.versoit.todoapp.di.module.DataModule
import ru.versoit.todoapp.di.module.DomainModule
import ru.versoit.todoapp.di.module.PresentationModule
import ru.versoit.todoapp.di.scopes.ActivityScope
import ru.versoit.todoapp.presentation.MainActivity

@ActivityScope
@Subcomponent(modules = [DomainModule::class, PresentationModule::class, DataModule::class])
interface MainActivityComponent {

    fun inject(mainActivity: MainActivity)
}