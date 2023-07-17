package ru.versoit.data.storage.datasources.local

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.versoit.data.models.toLocal
import ru.versoit.data.storage.room.entity.toData
import ru.versoit.data.storage.room.root.AppDatabase

/**
 * Implementation of LocalTOdoItemDataSource that handles
 * and retrieves todo items data from local storage.
 *
 * @param context
 */
class RoomTodoItemDataSource(context: Context) : LocalTodoItemDataSource {

    private val database: AppDatabase

    init {
        database = AppDatabase.getDatabase(context)
    }

    /**
     * Retrieves todo items list from local storage.
     *
     * @return Flow of list of TodoItemData.
     */
    override suspend fun getAllTodoItems(): Flow<List<ru.versoit.data.models.TodoItemData>> =
        database.todoItemDao().getAllTodoItems().map { value -> value.map { it.toData() } }

    /**
     * Adds a todo item in local storage.
     *
     * @param todoItem Todo item to add.
     */
    override suspend fun addTodoItem(todoItem: ru.versoit.data.models.TodoItemData) =
        database.todoItemDao().addTodoItem(todoItem.toLocal())

    /**
     * Updates todo item in local storage.
     *
     * @param todoItem Todo item to update.
     */
    override suspend fun updateTodoItem(todoItem: ru.versoit.data.models.TodoItemData) =
        database.todoItemDao().updateTodoItem(todoItem.toLocal())


    /**
     * Removes todo item by its id.
     *
     * @param id Removing todo item id.
     */
    override suspend fun removeTodoItem(id: String) = database.todoItemDao().deleteTodoItem(id)

    /**
     * Inserts list of todo items in local storage.
     *
     * @param todoItems List of todo items to insert.
     */
    override suspend fun insertAllTodoItems(todoItems: List<ru.versoit.data.models.TodoItemData>) {
        database.todoItemDao().insertAllTodoItems(todoItems.map { it.toLocal() })
    }
}