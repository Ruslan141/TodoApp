package ru.versoit.todoapp.data.storage.datasources

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.versoit.todoapp.data.models.TodoItemData
import ru.versoit.todoapp.data.models.toLocal
import ru.versoit.todoapp.data.storage.room.entity.toData
import ru.versoit.todoapp.data.storage.room.root.AppDatabase

class RoomTodoItemDataSource(context: Context) : LocalTodoItemDataSource {

    private val database: AppDatabase

    init {
        database = AppDatabase.getDatabase(context)
    }

    override suspend fun getAllTodoItems(): Flow<List<TodoItemData>> =
        database.todoItemDao().getAllTodoItems().map { value -> value.map { it.toData() } }

    override suspend fun addTodoItem(todoItem: TodoItemData) =
        database.todoItemDao().addTodoItem(todoItem.toLocal())

    override suspend fun updateTodoItem(todoItem: TodoItemData) =
        database.todoItemDao().updateTodoItem(todoItem.toLocal())

    override suspend fun removeTodoItem(id: String) = database.todoItemDao().deleteTodoItem(id)

    override suspend fun getTodoItemById(id: String): Flow<TodoItemData?> =
        database.todoItemDao().getTodoItemById(id).map { it.toData() }

    override suspend fun insertAllTodoItems(todoItems: List<TodoItemData>) {
        database.todoItemDao().insertAllTodoItems(todoItems.map { it.toLocal() })
    }

}