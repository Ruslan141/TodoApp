package ru.versoit.todoapp.data.storage.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.versoit.todoapp.data.storage.room.entity.TodoItemEntity

@Dao
interface TodoItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTodoItem(todoItem: TodoItemEntity)

    @Query("SELECT * FROM todoItems")
    fun getAllTodoItems(): Flow<List<TodoItemEntity>>

    @Query("DELETE FROM todoItems WHERE id = :id")
    suspend fun deleteTodoItem(id: String)

    @Update
    suspend fun updateTodoItem(todoItem: TodoItemEntity)

    @Query("SELECT * FROM todoItems WHERE id = :id")
    fun getTodoItemById(id: String): Flow<TodoItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTodoItems(todoItems: List<TodoItemEntity>)
}