package ru.versoit.todoapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.versoit.todoapp.domain.models.TodoItem
import ru.versoit.todoapp.domain.usecase.AddTodoItemUseCase
import ru.versoit.todoapp.domain.usecase.GetAllTodoItemsUseCase
import ru.versoit.todoapp.domain.usecase.TodoItemRemoveUseCase
import ru.versoit.todoapp.domain.usecase.TodoItemUpdateUseCase

class TodoItemsViewModel(
    private val todoItemUpdateUseCase: TodoItemUpdateUseCase,
    private val todoItemRemoveUseCase: TodoItemRemoveUseCase,
    private val addTodoItemUseCase: AddTodoItemUseCase,
    private val getAllTodoItemsUseCase: GetAllTodoItemsUseCase
) : ViewModel(), TodoItemUpdater, TodoItemRemover {

    private val _todoItems: MutableLiveData<List<TodoItem>> = MutableLiveData()
    val todoItemsObservable: LiveData<List<TodoItem>> = _todoItems

    private var todoItems = listOf<TodoItem>()

    private var lastDeleted: TodoItem? = null

    private val _isHidden = MutableLiveData(false)
    val isHidden: LiveData<Boolean> = _isHidden

    private val isHiddenValue get() = _isHidden.value!!

    init {
        loadTodoItems()
    }

    override fun updateTodoItem(todoItem: TodoItem) = todoItemUpdateUseCase.updateTodoItem(todoItem)

    val readyStatesAmount get() = todoItems.count { it.completed }

    private fun loadTodoItems() {

        viewModelScope.launch {
            getAllTodoItemsUseCase.getAllTodoItems().collect { it ->
                todoItems = it
                todoItems = todoItems.sortedByDescending { it.lastChanged }
                updateLiveData(todoItems)
            }
        }
    }

    fun isCompletedTodoItem(position: Int) = todoItems[position].completed

    private fun updateLiveData(todoItems: List<TodoItem>) {

        if (isHiddenValue) {
            _todoItems.value = todoItems.filter { !it.completed }
            return
        }

        _todoItems.value = todoItems
    }

    fun hideCompletedTodoItems() {
        _isHidden.value = true
        _todoItems.value = todoItems.filter { !it.completed }
    }

    override fun removeTodoItem(position: Int) {
        val todoItem = todoItems[position]
        lastDeleted = todoItem
        todoItemRemoveUseCase.removeTodoItem(todoItem.id)
    }

    fun setCompletedTodoItem(position: Int) {
        var todoItem = todoItems[position]
        todoItem = todoItem.copy(completed = true)
        todoItemUpdateUseCase.updateTodoItem(todoItem)
    }

    fun undoDeletedTodoItem() {
        lastDeleted?.let {
            addTodoItemUseCase.addTodoItem(it)
        }
    }

    fun showCompletedTodoItems() {
        _isHidden.value = false
        _todoItems.value = todoItems
    }
}