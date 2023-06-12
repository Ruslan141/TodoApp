package ru.versoit.todoapp.data.storage.datasources.mock

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import ru.versoit.todoapp.data.storage.datasources.TodoItemDataSource
import ru.versoit.todoapp.data.storage.models.TodoItemEntity
import ru.versoit.todoapp.domain.models.Importance
import java.util.Date

class MockTodoItemDataSource : TodoItemDataSource {

    private val todoItems: MutableList<TodoItemEntity> = mutableListOf(
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
        ),
        TodoItemEntity(
            "8",
            "asdfadsf что-то",
            Importance.LESS_IMPORTANT,
            Date(),
            true,
            Date(),
            Date()
        ),
        TodoItemEntity(
            "9",
            "sadfads что-то",
            Importance.LESS_IMPORTANT,
            Date(),
            true,
            Date(),
            Date()
        ),
        TodoItemEntity(
            "10",
            "Написать что-то",
            Importance.LESS_IMPORTANT,
            Date(),
            false,
            Date(),
            Date()
        ),
        TodoItemEntity(
            "11",
            "НЕВАЖНО",
            Importance.UNIMPORTANT,
            Date(),
            false,
            Date(),
            Date()
        ),
        TodoItemEntity("4", "Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet.", Importance.IMPORTANT, Date(), false, Date(), Date()),
        TodoItemEntity("4", "Тестовые данные. Тестовые данные. Тестовые данные. Тестовые данные. Тестовые данные. Тестовые данные. Тестовые данные. Тестовые данные. Тестовые данные. Тестовые данные. Тестовые данные. Тестовые данные. ", Importance.IMPORTANT, Date(), false, Date(), Date()),
        TodoItemEntity(
            "12",
            "Написать что-то",
            Importance.LESS_IMPORTANT,
            Date(),
            false,
            Date(),
            Date()
        ),
        TodoItemEntity(
            "13",
            "Написать что-то",
            Importance.LESS_IMPORTANT,
            Date(),
            false,
            Date(),
            Date()
        ),
        TodoItemEntity(
            "14",
            "Написать что-то",
            Importance.LESS_IMPORTANT,
            Date(),
            false,
            Date(),
            Date()
        ),
        TodoItemEntity(
            "15",
            "Написать что-то",
            Importance.LESS_IMPORTANT,
            Date(),
            false,
            Date(),
            Date()
        ),
        TodoItemEntity(
            "16",
            "Написать что-то",
            Importance.LESS_IMPORTANT,
            Date(),
            false,
            Date(),
            Date()
        ),
        TodoItemEntity(
            "17",
            "Написать что-то",
            Importance.LESS_IMPORTANT,
            Date(),
            false,
            Date(),
            Date()
        ),
        TodoItemEntity(
            "18",
            "Написать что-то",
            Importance.LESS_IMPORTANT,
            Date(),
            false,
            Date(),
            Date()
        ),
        TodoItemEntity(
            "19",
            "Написать что-то",
            Importance.LESS_IMPORTANT,
            Date(),
            false,
            Date(),
            Date()
        ),
        TodoItemEntity(
            "20",
            "Написать что-то",
            Importance.LESS_IMPORTANT,
            Date(),
            false,
            Date(),
            Date()
        ),
    )

    private val todoItemsFlow: MutableStateFlow<List<TodoItemEntity>> =
        MutableStateFlow(todoItems)

    override fun getAllTodoItems(): Flow<List<TodoItemEntity>> {
        return todoItemsFlow
    }

    override fun addTodoItem(todoItem: TodoItemEntity) {
        todoItems.add(todoItem)
        todoItemsFlow.value = todoItems
    }

    override fun updateTodoItem(todoItem: TodoItemEntity) {
        val prevIndex = todoItems.indexOf(todoItem)
        todoItems[prevIndex] = todoItem
        todoItemsFlow.value = todoItems
    }

    override fun removeTodoItem(id: String) {
        todoItems.removeIf { it.id == id }
        todoItemsFlow.value = todoItems
    }

    override fun getTodoItemById(id: String): Flow<TodoItemEntity?> {
        return todoItemsFlow.map { it -> it.find { it.id == id } }
    }
}