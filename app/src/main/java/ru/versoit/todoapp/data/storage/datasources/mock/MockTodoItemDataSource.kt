package ru.versoit.todoapp.data.storage.datasources.mock

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import ru.versoit.todoapp.data.storage.datasources.TodoItemDataSource
import ru.versoit.todoapp.data.storage.models.TodoItemEntity
import ru.versoit.todoapp.domain.models.Importance
import java.util.Date

object MockTodoItemDataSource : TodoItemDataSource {

    private var todoItems: List<TodoItemEntity> = createMockData()

    var idCounter = 14

    private fun createMockData(): List<TodoItemEntity> {

        return mutableListOf(

            TodoItemEntity(
                "1",
                "Lorem ipsum dolor sit amet, Lorem ipsum dolor sit amet, Lorem ipsum dolor sit amet",
                Importance.UNIMPORTANT,
                Date(),
                false,
                false,
                Date(),
                Date()
            ),
            TodoItemEntity(
                "2",
                "Lorem ipsum dolor sit amet",
                Importance.LESS_IMPORTANT,
                Date(),
                false,
                true,
                Date(),
                Date()
            ),
            TodoItemEntity(
                "3",
                "Lorem ipsum dolor sit amet, Lorem ipsum dolor sit amet, Lorem ipsum dolor sit amet.",
                Importance.LESS_IMPORTANT,
                Date(),
                false,
                false,
                Date(),
                Date()
            ),
            TodoItemEntity(
                "4",
                "Lorem ipsum dolor sit amet",
                Importance.IMPORTANT,
                Date(),
                false,
                false,
                Date(),
                Date()
            ),
            TodoItemEntity(
                "5",
                "Lorem ipsum dolor sit amet, Lorem ipsum dolor sit amet",
                Importance.IMPORTANT,
                Date(),
                false,
                false,
                Date(),
                Date()
            ),
            TodoItemEntity(
                "6",
                "Lorem ipsum dolor sit amet",
                Importance.LESS_IMPORTANT,
                Date(),
                false,
                false,
                Date(),
                Date()
            ),
            TodoItemEntity(
                "7",
                "Lorem ipsum dolor sit amet",
                Importance.IMPORTANT,
                Date(),
                false,
                false,
                Date(),
                Date()
            ),
            TodoItemEntity(
                "8",
                "Lorem ipsum dolor sit amet",
                Importance.UNIMPORTANT,
                Date(),
                false,
                true,
                Date(),
                Date()
            ),
            TodoItemEntity(
                "9",
                "Lorem ipsum dolor sit amet, Lorem ipsum dolor sit amet",
                Importance.LESS_IMPORTANT,
                Date(),
                false,
                false,
                Date(),
                Date()
            ),
            TodoItemEntity(
                "10",
                "Lorem ipsum dolor sit amet, Lorem ipsum dolor sit amet",
                Importance.UNIMPORTANT,
                Date(),
                false,
                false,
                Date(),
                Date()
            )
        )
    }

    private val todoItemsFlow: MutableStateFlow<List<TodoItemEntity>> =
        MutableStateFlow(todoItems)

    override fun getAllTodoItems(): Flow<List<TodoItemEntity>> {
        return todoItemsFlow
    }

    override fun addTodoItem(todoItem: TodoItemEntity) {
        if (todoItem.id.trim().isEmpty())
            todoItem.id = "${idCounter++}"

        val updatedList = todoItems.toMutableList()
        updatedList.add(todoItem)
        todoItems = updatedList.toList()
        todoItemsFlow.value = todoItems
    }

    override fun updateTodoItem(todoItem: TodoItemEntity) {
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