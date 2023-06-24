package ru.versoit.todoapp.presentation.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.versoit.todoapp.domain.usecase.TodoItemRemoveUseCase
import ru.versoit.todoapp.domain.usecase.TodoItemUpdateUseCase
import ru.versoit.todoapp.presentation.viewmodels.EditTodoItemViewModel

class EditTodoItemViewModelFactory(
    private val todoItemUpdateUseCase: TodoItemUpdateUseCase,
    private val todoItemRemoveUseCase: TodoItemRemoveUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditTodoItemViewModel(todoItemUpdateUseCase, todoItemRemoveUseCase) as T
    }
}