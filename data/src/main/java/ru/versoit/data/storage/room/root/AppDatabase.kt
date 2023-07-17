package ru.versoit.data.storage.room.root

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.versoit.data.storage.room.DateConverter
import ru.versoit.data.storage.room.dao.TodoItemDao
import ru.versoit.data.storage.room.entity.TodoItemEntity

/**
 * Represents a root application data base class.
 */
@Database(entities = [TodoItemEntity::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Gets dao of todo items.
     */
    abstract fun todoItemDao(): TodoItemDao

    companion object {

        @Volatile
        private var Instance: AppDatabase? = null

        /**
         * Gets instance of database.
         *
         * @return AppDatabase.
         */
        fun getDatabase(context: Context): AppDatabase {

            return Instance ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "todo_items_database"
                ).build()

                Instance = instance
                instance
            }
        }
    }
}