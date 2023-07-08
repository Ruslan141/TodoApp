package ru.versoit.data.storage.datasources.network

import kotlinx.coroutines.flow.Flow
import ru.versoit.data.models.TodoItemData

/**
 * Represents a remote todo item response.
 *
 * @property token OAuth user token.
 */
interface RemoteTodoItemDataSource {

    companion object {
        const val OK = "ok"
    }

    /**
     * A suspend function that returns a flow of all todo items.
     *
     * @return A Flow emitting list of todo items.
     */
    suspend fun getAllTodoItems(): Flow<Result<TodoItemsResponse>>

    /**
     * A suspend function to add a new todo item.
     *
     * @param todoItem The todo item to be added.
     */
    suspend fun addTodoItem(todoItem: ru.versoit.data.models.TodoItemData, revision: Int, status: String): Result<TodoItemsResponse>

    /**
     * A suspend function to update an existing todo item.
     *
     * @param todoItem The updated todo item.
     */
    suspend fun updateTodoItem(todoItem: ru.versoit.data.models.TodoItemData, revision: Int, status: String): Result<TodoItemResponse>

    /**
     * A function that removes todo item by its id.
     *
     * @param id The id of the todo item to be removed.
     */
    suspend fun removeTodoItem(id: String, revision: Int): Result<TodoItemResponse>

    /**
     * Updates all todo items.
     *
     * @param todoItems The list of TodoItemData to be updated.
     * @param status The status of the TodoItems.
     * @param revision The current revision number.
     * @return A Result containing the updated revision number.
     */
    suspend fun updateAllTodoItems(todoItems: List<ru.versoit.data.models.TodoItemData>, status: String, revision: Int): Result<Int>

    var token: String?
}

/**
 * A data class represents a todo item response from network.
 *
 * @property data The todo item in data-layer.
 * @property lastRevision The last revision number.
 */
data class TodoItemResponse(val data: ru.versoit.data.models.TodoItemData?, val lastRevision: Int)

/**
 * A data class represents a todo item list response from network.
 *
 * @property data The todo item list in data-layer.
 * @property lastRevision The last revision number.
 */
data class TodoItemsResponse(val data: List<ru.versoit.data.models.TodoItemData>, val lastRevision: Int)