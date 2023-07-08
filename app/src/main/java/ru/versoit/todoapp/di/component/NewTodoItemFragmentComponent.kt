package ru.versoit.todoapp.di.component

import dagger.Subcomponent
import ru.versoit.todoapp.di.module.DataModule
import ru.versoit.todoapp.di.module.DomainModule
import ru.versoit.todoapp.di.module.PresentationModule
import ru.versoit.todoapp.di.scopes.FragmentScope
import ru.versoit.todoapp.presentation.fragments.NewTodoItemFragment

@FragmentScope
@Subcomponent(modules = [DomainModule::class, PresentationModule::class, DataModule::class])
interface NewTodoItemFragmentComponent {

    fun inject(fragment: NewTodoItemFragment)
}