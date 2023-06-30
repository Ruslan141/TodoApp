package ru.versoit.todoapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
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

    private val _hideCompleted: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val hideCompleted: Flow<Boolean> = _hideCompleted

    private var lastDeleted: TodoItem? = null

    override fun updateTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            todoItemUpdateUseCase(todoItem)
        }
    }

    suspend fun getReadyStatesAmount() = getAllTodoItems().map { list -> list.count{ it.done } }

    suspend fun synchronizeWithNetwork() {
        networkSynchronizer.synchronizeWithNetwork()
    }

    fun hideCompletedTodoItems() {
        _hideCompleted.value = true
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

    suspend fun getAllTodoItems(): Flow<List<TodoItem>> {
        return if (_hideCompleted.value) {
            getAllTodoItemsUseCase().map { list -> list.filter { element -> element.done }  }
        } else {
            getAllTodoItemsUseCase()
        }
    }

    fun setSyncFailureCallback(action: suspend () -> Unit) {
        syncCallback.onSyncFailure = action
    }

    fun showCompletedTodoItems() {
        _hideCompleted.value = false
    }

    fun isTodoItemsHidden(): Boolean {
        return _hideCompleted.value
    }
}