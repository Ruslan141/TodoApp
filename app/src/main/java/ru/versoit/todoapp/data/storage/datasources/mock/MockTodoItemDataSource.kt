package ru.versoit.todoapp.data.storage.datasources.mock

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import ru.versoit.todoapp.data.storage.datasources.TodoItemDataSource
import ru.versoit.todoapp.data.storage.models.TodoItemEntity
import ru.versoit.todoapp.domain.models.Importance
import java.util.Date

object MockTodoItemDataSource : TodoItemDataSource {

    private var todoItems: List<TodoItemEntity> = mutableListOf(
        TodoItemEntity(
            "0",
            "Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet.",
            Importance.IMPORTANT,
            Date(),
            false,
            Date(),
            Date()
        ),
        TodoItemEntity(
            "1",
            "Написать что-то",
            Importance.LESS_IMPORTANT,
            Date(),
            true,
            Date(),
            Date()
        ),
        TodoItemEntity(
            "2",
            "Написать что-то",
            Importance.LESS_IMPORTANT,
            Date(),
            true,
            Date(),
            Date()
        ),
        TodoItemEntity(
            "3",
            "Написать что-то",
            Importance.LESS_IMPORTANT,
            Date(),
            true,
            Date(),
            Date()
        ),
        TodoItemEntity(
            "4",
            "Написать что-то",
            Importance.LESS_IMPORTANT,
            Date(),
            true,
            Date(),
            Date()
        ),
        TodoItemEntity(
            "5",
            "Написать что-то",
            Importance.LESS_IMPORTANT,
            Date(),
            true,
            Date(),
            Date()
        ),
        TodoItemEntity(
            "6",
            "Написать что-то",
            Importance.LESS_IMPORTANT,
            Date(),
            true,
            Date(),
            Date()
        ),
        TodoItemEntity(
            "7",
            "Написать что-то",
            Importance.LESS_IMPORTANT,
            Date(),
            true,
            Date(),
            Date()
        )
    )

    private val todoItemsFlow: MutableStateFlow<List<TodoItemEntity>> =
        MutableStateFlow(todoItems)

    override fun getAllTodoItems(): Flow<List<TodoItemEntity>> {
        return todoItemsFlow
    }

    override fun addTodoItem(todoItem: TodoItemEntity) {
        todoItem.id = "${todoItems.size + 1}"
        val updatedList = todoItems.toMutableList()
        updatedList.add(todoItem)
        todoItems = updatedList.toList()
        todoItemsFlow.value = todoItems
    }

    override fun updateTodoItem(todoItem: TodoItemEntity) {
        Log.e("Update todoItem", "update todo")
        val index = todoItems.indexOfFirst { it.id == todoItem.id }
        val newList = todoItems.toMutableList()
        newList[index] = todoItem
        todoItems = newList.toList()
        todoItemsFlow.value = todoItems
    }

    override fun removeTodoItem(id: String) {
        val newList = todoItems.toMutableList()
        newList.removeIf { it.id == id }
        todoItems = newList.toList()
        todoItemsFlow.value = newList
    }

    override fun getTodoItemById(id: String): Flow<TodoItemEntity?> {
        return todoItemsFlow.map { it -> it.find { it.id == id } }
    }
}