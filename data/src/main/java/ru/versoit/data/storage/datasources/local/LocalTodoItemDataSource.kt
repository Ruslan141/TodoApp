package ru.versoit.data.storage.datasources.local

import kotlinx.coroutines.flow.Flow
import ru.versoit.data.models.TodoItemData

/**
 * Represents a local todo item data source.
 */
interface LocalTodoItemDataSource {

    /**
     * Retrieves todo items list from local storage.
     *
     * @return Flow of list of TodoItemData.
     */
    suspend fun getAllTodoItems(): Flow<List<ru.versoit.data.models.TodoItemData>>

    /**
     * Adds a todo item in local storage.
     *
     * @param todoItem Todo item to add.
     */
    suspend fun addTodoItem(todoItem: ru.versoit.data.models.TodoItemData)

    /**
     * Updates todo item in local storage.
     *
     * @param todoItem Todo item to update.
     */
    suspend fun updateTodoItem(todoItem: ru.versoit.data.models.TodoItemData)

    /**
     * Removes todo item by its id.
     *
     * @param id Removing todo item id.
     */
    suspend fun removeTodoItem(id: String)

    /**
     * Inserts list of todo items in local storage.
     *
     * @param todoItems List of todo items to insert.
     */
    suspend fun insertAllTodoItems(todoItems: List<ru.versoit.data.models.TodoItemData>)
}