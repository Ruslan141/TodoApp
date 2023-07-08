package ru.versoit.todoapp.presentation.features.vmfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.versoit.domain.usecase.AddTodoItemUseCase
import ru.versoit.todoapp.presentation.viewmodels.NewTodoItemViewModel

class NewTodoItemViewModelFactory(private val addTodoItemUseCase: AddTodoItemUseCase) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewTodoItemViewModel(addTodoItemUseCase) as T
    }
}