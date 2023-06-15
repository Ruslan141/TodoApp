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

    lateinit var lastDeleted: TodoItem
        private set

    lateinit var lastCompleted: TodoItem
        private set

    private val _isHidden = MutableLiveData<Boolean>(false)
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
                todoItems = todoItems.sortedBy { it.id }
                updateLiveData(todoItems)
            }
        }
    }

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
        val todoItem = todoItems[position]
        todoItem.completed = true
        lastCompleted = todoItem
        todoItemRemoveUseCase.removeTodoItem(todoItem.id)
    }

    fun undoDeletedTodoItem() = addTodoItemUseCase.addTodoItem(lastDeleted)

    fun undoCompletedTodoItem() {
        lastCompleted.completed = false
        addTodoItemUseCase.addTodoItem(lastCompleted)
    }


    fun showCompletedTodoItems() {
        _isHidden.value = false
        _todoItems.value = todoItems
    }
}