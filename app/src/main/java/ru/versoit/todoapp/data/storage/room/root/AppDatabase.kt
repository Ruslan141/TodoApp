package ru.versoit.todoapp.data.storage.room.root

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.versoit.todoapp.data.storage.room.DateConverter
import ru.versoit.todoapp.data.storage.room.dao.TodoItemDao
import ru.versoit.todoapp.data.storage.room.entity.TodoItemEntity

@Database(entities = [TodoItemEntity::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun todoItemDao(): TodoItemDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "todo_items_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}