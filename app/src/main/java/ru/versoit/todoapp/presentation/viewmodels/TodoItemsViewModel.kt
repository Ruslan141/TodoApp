package ru.versoit.todoapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.versoit.todoapp.domain.models.TodoItem
import ru.versoit.todoapp.domain.repository.NetworkSynchronizer
import ru.versoit.todoapp.domain.repository.SyncCallback
import ru.versoit.todoapp.domain.usecase.AddTodoItemUseCase
import ru.versoit.todoapp.domain.usecase.GetAllTodoItemsUseCase
import ru.versoit.todoapp.domain.usecase.TodoItemRemoveUseCase
import ru.versoit.todoapp.domain.usecase.TodoItemUpdateUseCase

class TodoItemsViewModel(
    private val todoItemUpdateUseCase: TodoItemUpdateUseCase,
    private val todoItemRemoveUseCase: TodoItemRemoveUseCase,
    private val addTodoItemUseCase: AddTodoItemUseCase,
    private val getAllTodoItemsUseCase: GetAllTodoItemsUseCase,
    private val networkSynchronizer: NetworkSynchronizer,
    private val syncCallback: SyncCallback,
) : ViewModel(), TodoItemUpdater, TodoItemRemover {

    private val _hideCompleted: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val hideCompleted: Flow<Boolean> = _hideCompleted

    private var todoItemsList = listOf<TodoItem>()

    private val _todoItemsFlow: MutableStateFlow<List<TodoItem>?> = MutableStateFlow(null)
    val todoItemsFlow: Flow<List<TodoItem>?> = _todoItemsFlow

    private val _todoItemsDoneAmount = MutableStateFlow(0)
    val todoItemsDoneAmount: Flow<Int> = _todoItemsDoneAmount

    private var lastDeleted: TodoItem? = null

    override fun updateTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            todoItemUpdateUseCase(todoItem)
        }
    }

    fun loadTodoItems() {

        viewModelScope.launch {
            getAllTodoItemsUseCase().collect { list ->
                todoItemsList = list.sortedByDescending { todoItem -> todoItem.created }
                _todoItemsFlow.value = todoItemsList

                _todoItemsDoneAmount.value = todoItemsList.count { todoItem -> todoItem.done }

                if (_hideCompleted.value) {
                    _todoItemsFlow.value = todoItemsList.filterNot { todoItem -> todoItem.done }
                }
            }
        }
    }

    suspend fun synchronizeWithNetwork() {
        networkSynchronizer.synchronizeWithNetwork()
    }

    fun hideCompletedTodoItems() {
        _hideCompleted.value = true
        _todoItemsFlow.value = todoItemsList.filterNot { todoItem -> todoItem.done }
    }

    override fun removeTodoItem(todoItem: TodoItem) {

        viewModelScope.launch {
            todoItemRemoveUseCase(todoItem.id)
            lastDeleted = todoItem
        }
    }

    fun setCompletedTodoItem(todoItem: TodoItem) {

        viewModelScope.launch {
            todoItemUpdateUseCase(todoItem.copy(done = true))
        }
    }

    fun undoDeletedTodoItem() {

        viewModelScope.launch {
            lastDeleted?.let {
                addTodoItemUseCase(it)
            }
        }
    }

    fun setSyncFailureCallback(action: suspend () -> Unit) {
        syncCallback.onSyncFailure = action
    }

    fun showCompletedTodoItems() {
        _todoItemsFlow.value = todoItemsList
        _hideCompleted.value = false
    }

    fun isTodoItemsHidden(): Boolean {
        return _hideCompleted.value
    }
}