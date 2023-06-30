package ru.versoit.todoapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import ru.versoit.todoapp.data.models.TodoItemData
import ru.versoit.todoapp.data.models.toDomain
import ru.versoit.todoapp.data.storage.datasources.DifferentRevisionsException
import ru.versoit.todoapp.data.storage.datasources.LocalTodoItemDataSource
import ru.versoit.todoapp.data.storage.datasources.RemoteTodoItemDataSource
import ru.versoit.todoapp.data.storage.datasources.RevisionDataSource
import ru.versoit.todoapp.data.storage.datasources.TodoItemsResponse
import ru.versoit.todoapp.domain.models.TodoItem
import ru.versoit.todoapp.domain.repository.InternetFailure
import ru.versoit.todoapp.domain.repository.NetworkSynchronizer
import ru.versoit.todoapp.domain.repository.SyncCallback
import ru.versoit.todoapp.domain.repository.TodoItemRepository
import ru.versoit.todoapp.utils.toData

class TodoItemRepositoryImpl(
    private val todoItemLocalDataSource: LocalTodoItemDataSource,
    private val todoItemRemoteDataSource: RemoteTodoItemDataSource,
    private val revisionDataSource: RevisionDataSource,
) : TodoItemRepository, SyncCallback, InternetFailure, NetworkSynchronizer {

    override var onSyncSuccess: suspend () -> Unit = {}

    override var onSyncFailure: suspend () -> Unit = {}

    override var onInternetFailure: suspend () -> Unit = {}

    override suspend fun getAllTodoItems(): Flow<List<TodoItem>> {

        handleNetworkOperation {
            synchronizeWithNetwork()
        }

        return todoItemLocalDataSource.getAllTodoItems()
            .map { it.map { value -> value.toDomain() } }
    }

    override suspend fun addTodoItem(todoItem: TodoItem) {
        todoItemLocalDataSource.addTodoItem(todoItem.toData())

        handleNetworkOperation {
            todoItemRemoteDataSource.addTodoItem(
                todoItem.toData(),
                revisionDataSource.getValue(),
                RemoteTodoItemDataSource.OK
            )
        }
    }

    private suspend fun handleNetworkOperation(action: suspend () -> Unit) {

        try {
            action()
        } catch (exception: DifferentRevisionsException) {
            synchronizeWithNetwork()
        } catch (exception: Exception) {
            onInternetFailure()
        }
    }

    override suspend fun updateTodoItem(todoItem: TodoItem) {

        handleNetworkOperation {
            todoItemLocalDataSource.updateTodoItem(todoItem.toData())

            val actualRevision = todoItemRemoteDataSource.updateTodoItem(
                todoItem.toData(),
                revisionDataSource.getValue(),
                RemoteTodoItemDataSource.OK
            ).lastRevision

            revisionDataSource.save(actualRevision)
        }
    }

    override suspend fun removeTodoItem(id: String) {

        handleNetworkOperation {
            val actualRevision = todoItemRemoteDataSource.removeTodoItem(
                id,
                revisionDataSource.getValue()
            ).lastRevision
            revisionDataSource.save(actualRevision)
        }
        todoItemLocalDataSource.removeTodoItem(id)
    }

    override suspend fun getTodoItemById(id: String) =
        todoItemLocalDataSource.getTodoItemById(id).map { it?.toDomain() }

    override suspend fun synchronizeWithNetwork(): Boolean {
        val localDataFlow = todoItemLocalDataSource.getAllTodoItems()
        val remoteDataFlow = todoItemRemoteDataSource.getAllTodoItems()

        val mergedFlow = combine(localDataFlow, remoteDataFlow) { localData, remoteData ->

            val merged = mergeTodoItems(localData, remoteData.data, remoteData.lastRevision)

            TodoItemsResponse(merged, remoteData.lastRevision)
        }.take(1)

        return tryUpdateTodoItems(mergedFlow)
    }

    private suspend fun tryUpdateTodoItems(
        mergedFlow: Flow<TodoItemsResponse>
    ): Boolean {
        try {
            mergedFlow.collect { response ->

                todoItemLocalDataSource.insertAllTodoItems(response.data)

                val actualRevision = todoItemRemoteDataSource.updateAllTodoItems(
                    response.data,
                    RemoteTodoItemDataSource.OK,
                    revisionDataSource.getValue()
                )

                revisionDataSource.save(actualRevision)
                onSyncSuccess()
            }
            return true
        } catch (exception: Exception) {
            onSyncFailure()
            return false
        }
    }

    private suspend fun mergeTodoItems(
        local: List<TodoItemData>,
        remote: List<TodoItemData>,
        lastRevision: Int
    ): List<TodoItemData> {

        if (lastRevision == revisionDataSource.getValue()) {
            return local
        }

        return (local + remote).groupBy { it.id }.map { it.value.maxBy { value -> value.lastUpdate } }
    }
}