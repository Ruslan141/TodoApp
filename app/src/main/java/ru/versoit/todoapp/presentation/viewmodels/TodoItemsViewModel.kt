package ru.versoit.todoapp.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.versoit.todoapp.domain.models.TodoItem
import ru.versoit.todoapp.domain.usecase.GetAllTodoItemsUseCase
import ru.versoit.todoapp.domain.usecase.GetTodoItemByIdUseCase
import ru.versoit.todoapp.domain.usecase.TodoItemRemoveUseCase
import ru.versoit.todoapp.domain.usecase.TodoItemUpdateUseCase

class TodoItemsViewModel(
    private val todoItemUpdateUseCase: TodoItemUpdateUseCase,
    private val todoItemRemoveUseCase: TodoItemRemoveUseCase,
    private val getTodoItemByIdUseCase: GetTodoItemByIdUseCase,
    private val getAllTodoItemsUseCase: GetAllTodoItemsUseCase
) : ViewModel(), TodoItemUpdater {

    private val _todoItems: MutableLiveData<List<TodoItem>> = MutableLiveData()
    val todoItems: LiveData<List<TodoItem>> = _todoItems

    init {
        loadTodoItems()
    }

    override fun updateTodoItem(todoItem: TodoItem) = todoItemUpdateUseCase.updateTodoItem(todoItem)

    val readyStatesAmount get() = todoItems.value?.count { it.state }

    private fun loadTodoItems() {

        viewModelScope.launch {
            getAllTodoItemsUseCase.getAllTodoItems().collect { it ->
                _todoItems.value = it
            }
        }
    }
}