package ru.versoit.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.withContext
import ru.versoit.data.models.TodoItemData
import ru.versoit.data.models.toDomain
import ru.versoit.data.storage.datasources.SyncException
import ru.versoit.data.storage.datasources.local.LocalTodoItemDataSource
import ru.versoit.data.storage.datasources.local.RevisionDataSource
import ru.versoit.data.storage.datasources.local.TokenDataSource
import ru.versoit.data.storage.datasources.network.InternetFailure
import ru.versoit.data.storage.datasources.network.NetworkSynchronizer
import ru.versoit.data.storage.datasources.network.RemoteTodoItemDataSource
import ru.versoit.data.storage.datasources.network.SyncCallback
import ru.versoit.data.storage.datasources.network.TodoItemsResponse
import ru.versoit.data.toData
import ru.versoit.domain.models.TodoItem
import ru.versoit.domain.repository.TodoItemRepository

/**
 * Implementation of the TodoItemRepository interface that handles data retrieval and manipulation
 * by utilizing local and remote data sources. It also implements SyncCallback, InternetFailure and
 * NetworkSynchronizer interfaces.
 *
 * @param todoItemLocalDataSource The local data source for todo item objects.
 * @param todoItemRemoteDataSource The remote data source for todo item objects.
 * @param revisionDataSource The data source for revision information.
 * @param tokenDataSource The data source for token information.
 *
 * @property onSyncSuccess The callback of network synchronization success.
 * @property onSyncFailure The callback of network synchronization failure.
 * @property onInternetFailure The callback of internet failure.
 */
class TodoItemRepositoryImpl(
    private val todoItemLocalDataSource: LocalTodoItemDataSource,
    private val todoItemRemoteDataSource: RemoteTodoItemDataSource,
    private val revisionDataSource: RevisionDataSource,
    private val tokenDataSource: TokenDataSource
) : TodoItemRepository, SyncCallback, InternetFailure, NetworkSynchronizer {

    override var onSyncSuccess: suspend () -> Unit = {}

    override var onSyncFailure: suspend () -> Unit = {}

    override var onInternetFailure: suspend () -> Unit = {}

    /**
     * A suspend function that returns a Flow of all todo items.
     *
     * @return A Flow emitting list of todo items.
     */
    override suspend fun getAllTodoItems(): Flow<List<TodoItem>> {

        todoItemRemoteDataSource.token = tokenDataSource.getValue()

        synchronizeWithNetwork()

        return todoItemLocalDataSource.getAllTodoItems()
            .map { it.map { value -> value.toDomain() } }
    }

    /**
     * A suspend function to add a new todo item.
     *
     * @param todoItem The todo item to be added.
     */
    override suspend fun addTodoItem(todoItem: TodoItem) {
        todoItemLocalDataSource.addTodoItem(todoItem.toData())
        todoItemRemoteDataSource.addTodoItem(
            todoItem.toData(),
            revisionDataSource.getValue(),
            RemoteTodoItemDataSource.OK
        )
    }

    /**
     * A suspend function to update an existing todo item.
     *
     * @param todoItem The updated todo item.
     */
    override suspend fun updateTodoItem(todoItem: TodoItem) {

        withContext(SupervisorJob() + Dispatchers.IO) {
            todoItemLocalDataSource.updateTodoItem(todoItem.toData())
        }

        val response = todoItemRemoteDataSource.updateTodoItem(
            todoItem.toData(),
            revisionDataSource.getValue(),
            RemoteTodoItemDataSource.OK
        )

        if (response.isSuccess) {
            revisionDataSource.save(response.getOrNull()!!.lastRevision)
        } else {
            handleResultFailure(response)
        }
    }

    /**
     * A function that removes todo item by its id.
     *
     * @param id The id of the todo item to be removed.
     */
    override suspend fun removeTodoItem(id: String) {

        withContext(SupervisorJob() + Dispatchers.IO) {
            todoItemLocalDataSource.removeTodoItem(id)
        }
        val result = todoItemRemoteDataSource.removeTodoItem(
            id,
            revisionDataSource.getValue()
        )

        if (result.isSuccess) {
            revisionDataSource.save(result.getOrNull()!!.lastRevision)
        } else {
            handleResultFailure(result)
        }
    }

    private suspend fun <T> handleResultFailure(result: Result<T>) {

        if (result.isFailure) {
            when (result.exceptionOrNull()) {
                is SyncException -> onSyncFailure()
                else -> onInternetFailure()
            }
        }
    }

    /**
     * A suspend function to synchronize data with network.
     */
    override suspend fun synchronizeWithNetwork(): Boolean {
        val localDataFlow = todoItemLocalDataSource.getAllTodoItems()
        val remoteDataFlow = todoItemRemoteDataSource.getAllTodoItems()

        val mergedFlow = combine(localDataFlow, remoteDataFlow) { localData, remoteData ->

            val response = remoteData.getOrNull()!!

            val merged = mergeTodoItems(localData, response.data, response.lastRevision)
            TodoItemsResponse(merged, response.lastRevision)
        }.take(1)

        return tryUpdateTodoItems(mergedFlow)
    }

    private suspend fun tryUpdateTodoItems(
        mergedFlow: Flow<TodoItemsResponse>
    ): Boolean {
        try {
            mergedFlow.collect { response ->

                withContext(SupervisorJob() + Dispatchers.IO) {
                    todoItemLocalDataSource.insertAllTodoItems(response.data)
                }

                val actualRevision = todoItemRemoteDataSource.updateAllTodoItems(
                    response.data,
                    RemoteTodoItemDataSource.OK,
                    revisionDataSource.getValue()
                )

                revisionDataSource.save(actualRevision.getOrDefault(0))
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

        return (local + remote).groupBy { it.id }
            .map { it.value.maxBy { value -> value.lastUpdate } }
    }
}