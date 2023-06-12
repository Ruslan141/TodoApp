package ru.versoit.todoapp.presentation.vmfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.versoit.todoapp.domain.repository.TodoItemRepository
import ru.versoit.todoapp.presentation.viewmodels.TodoItemsViewModel

class TodoItemsViewModelFactory(private val todoItemRepository: TodoItemRepository) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        TodoItemsViewModel(todoItemRepository) as T
}