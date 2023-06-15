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

    private fun createMockData(): List<TodoItemEntity> {
        val list = mutableListOf<TodoItemEntity>()

        for (idxNum in 0 until 20) {
            list.add(TodoItemEntity("$idxNum", "$idxNum", Importance.values()[(Math.random() * 10 % 3).toInt()], Date(), false,
                isDeadline = false,
                dateCreate = Date(),
                dateChange = Date()
            ))
        }

        return list
    }

    private val todoItemsFlow: MutableStateFlow<List<TodoItemEntity>> =
        MutableStateFlow(todoItems)

    override fun getAllTodoItems(): Flow<List<TodoItemEntity>> {
        return todoItemsFlow
    }

    override fun addTodoItem(todoItem: TodoItemEntity) {
        if (todoItem.id.trim().isEmpty())
            todoItem.id = "${todoItems.size}"

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