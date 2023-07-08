package ru.versoit.todoapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.versoit.data.storage.datasources.network.NetworkSynchronizer
import ru.versoit.data.storage.datasources.network.SyncCallback
import ru.versoit.domain.usecase.AddTodoItemUseCase
import ru.versoit.domain.usecase.GetAllTodoItemsUseCase
import ru.versoit.domain.usecase.TodoItemRemoveUseCase
import ru.versoit.domain.usecase.TodoItemUpdateUseCase

/**
 * ViewModel class for managing list of all todo items.
 *
 * @param todoItemUpdateUseCase The use case for updating todo item.
 * @param todoItemRemoveUseCase The use case for removing todo item.
 * @param addTodoItemUseCase The use case for adding todo item.
 * @param getAllTodoItemsUseCase The use case for getting all todo items.
 * @param networkSynchronizer The network synchronizer for syncing todo items with local data.
 * @param syncCallback The callback for handling sync callbacks.
 */

class TodoItemsViewModel(
    private val todoItemUpdateUseCase: TodoItemUpdateUseCase,
    private val todoItemRemoveUseCase: TodoItemRemoveUseCase,
    private val addTodoItemUseCase: AddTodoItemUseCase,
    private val getAllTodoItemsUseCase: GetAllTodoItemsUseCase,
    private val networkSynchronizer: NetworkSynchronizer,
    private val syncCallback: SyncCallback,
) : ViewModel(), TodoItemUpdater, TodoItemRemover, TodoItemCompleter, UndoDeleter {

    private val _hideCompleted: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val hideCompleted: Flow<Boolean> = _hideCompleted

    private var todoItemsList = listOf<ru.versoit.domain.models.TodoItem>()

    private val _todoItemsFlow: MutableStateFlow<List<ru.versoit.domain.models.TodoItem>?> = MutableStateFlow(null)
    val todoItemsFlow: Flow<List<ru.versoit.domain.models.TodoItem>?> = _todoItemsFlow

    private val _todoItemsDoneAmount = MutableStateFlow(0)
    val todoItemsDoneAmount: Flow<Int> = _todoItemsDoneAmount

    private var lastDeleted: ru.versoit.domain.models.TodoItem? = null

    /**
     * Replaces the element passed as a method parameter with an element that is in the store with the same id.
     *
     * This method delegates the tasks of updating to the [todoItemUpdateUseCase].
     */
    override fun updateTodoItem(todoItem: ru.versoit.domain.models.TodoItem) {
        viewModelScope.launch {
            todoItemUpdateUseCase(todoItem)
        }
    }

    /**
     * Loads all todo items from repository.
     */

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

    /**
     * Synchronize remote and local todo items.
     */
    suspend fun synchronizeWithNetwork() {
        networkSynchronizer.synchronizeWithNetwork()
    }

    /**
     * Hides completed todo items.
     */
    fun hideCompletedTodoItems() {
        _hideCompleted.value = true
        _todoItemsFlow.value = todoItemsList.filterNot { todoItem -> todoItem.done }
    }

    /**
     * Removes todo item.
     *
     * @param todoItem Todo item to remove.
     */
    override fun removeTodoItem(todoItem: ru.versoit.domain.models.TodoItem) {

        viewModelScope.launch {
            todoItemRemoveUseCase(todoItem.id)
            lastDeleted = todoItem
        }
    }

    /**
     * Mark todo item completed.
     *
     * @param todoItem todo item to readiness.
     */
    override fun setCompletedTodoItem(todoItem: ru.versoit.domain.models.TodoItem) {

        viewModelScope.launch {
            todoItemUpdateUseCase(todoItem.copy(done = true))
        }
    }

    /**
     * Restores last deleted todo item.
     */
    override fun undoDeletedTodoItem() {

        viewModelScope.launch {
            lastDeleted?.let {
                addTodoItemUseCase(it)
            }
        }
    }

    /**
     * Sets sync failure callback.
     *
     * @param action The action to be taken on a sync error.
     */
    fun setSyncFailureCallback(action: suspend () -> Unit) {
        syncCallback.onSyncFailure = action
    }

    /**
     * Shows all todo items.
     */
    fun showCompletedTodoItems() {
        _todoItemsFlow.value = todoItemsList
        _hideCompleted.value = false
    }

    /**
     * Checks is completed todo items is hidden.
     */
    fun isCompletedTodoItemsHidden(): Boolean {
        return _hideCompleted.value
    }
}