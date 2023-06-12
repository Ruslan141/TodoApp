package ru.versoit.todoapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.versoit.todoapp.domain.models.TodoItem
import ru.versoit.todoapp.domain.repository.TodoItemRepository

class TodoItemsViewModel(private val todoItemRepository: TodoItemRepository) : ViewModel() {

    private val _todoItems: MutableLiveData<List<TodoItem>> = MutableLiveData()
    val todoItems: LiveData<List<TodoItem>> = _todoItems

    init {
        loadTodoItems()
    }

    private fun loadTodoItems() {

        viewModelScope.launch {
            todoItemRepository.getAllTodoItems().collect { _todoItems.value = it }
        }
    }
}