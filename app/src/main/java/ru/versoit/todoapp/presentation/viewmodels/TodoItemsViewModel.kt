package ru.versoit.todoapp.presentation.viewmodels

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
    val todoItemsObservable: LiveData<List<TodoItem>> = _todoItems

    private var todoItems = listOf<TodoItem>()

    var isHidden: Boolean = false
        private set

    init {
        loadTodoItems()
    }

    override fun updateTodoItem(todoItem: TodoItem) = todoItemUpdateUseCase.updateTodoItem(todoItem)

    val readyStatesAmount get() = todoItems.count { it.state }

    private fun loadTodoItems() {

        viewModelScope.launch {
            getAllTodoItemsUseCase.getAllTodoItems().collect { it ->
                todoItems = it
                updateLiveData(todoItems)
            }
        }
    }

    private fun updateLiveData(todoItems: List<TodoItem>) {

        if (isHidden) {
            _todoItems.value = todoItems.filter { !it.state }
            return
        }

        _todoItems.value = todoItems
    }

    fun hideCompletedTodoItems() {
        isHidden = true
        _todoItems.value = todoItems.filter { !it.state }
    }

    fun showCompletedTodoItems() {
        isHidden = false
        _todoItems.value = todoItems
    }
}