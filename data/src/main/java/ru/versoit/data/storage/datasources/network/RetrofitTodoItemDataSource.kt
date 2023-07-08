package ru.versoit.data.storage.datasources.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.versoit.data.storage.datasources.SyncException
import ru.versoit.data.storage.retrofit.TaskApiService
import ru.versoit.data.storage.retrofit.dto.TodoItemContainer
import ru.versoit.data.storage.retrofit.dto.TodoItemDTO
import ru.versoit.data.storage.retrofit.dto.TodoItemsContainer
import ru.versoit.data.storage.retrofit.dto.toData

/**
 *  Implementation of the RemoteTodoItemDataSource interface
 *  using Retrofit for remote data retrieval and manipulation.
 */
class RetrofitTodoItemDataSource : RemoteTodoItemDataSource {

    companion object {

        const val BASE_URL: String = "https://beta.mrdekk.ru/todobackend/"

        const val AUTHORIZATION = "Authorization"

        const val OAUTH = "OAuth"
    }

    override var token: String? = null
        set(value) {
            field = value
            setupSettings()
        }

    private var interceptor: Interceptor? = null

    private var client: OkHttpClient? = null

    private var retrofit: Retrofit? = null

    private var todoItemApi: TaskApiService? = null

    /**
     * Sets up Retrofit settings, including the interceptor, OkHttpClient and Retrofit instance
     */
    private fun setupSettings() {

        initInterceptor()
        initClient()
        initRetrofit()

        todoItemApi = retrofit!!.create(TaskApiService::class.java)
    }

    private fun initInterceptor() {
        interceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader(AUTHORIZATION, "$OAUTH $token")
                .build()

            chain.proceed(request)
        }
    }

    private fun initClient() {
        client = OkHttpClient.Builder()
            .addInterceptor(interceptor!!)
            .build()
    }

    private fun initRetrofit() {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client!!)
            .build()
    }

    /**
     * Retrieves all TodoItems from the remote data source.
     *
     * @return A Flow emitting a Result containing a TodoItemsResponse.
     */
    override suspend fun getAllTodoItems(): Flow<Result<TodoItemsResponse>> = flow {
        emit(handleNetworkResult {
            val response = todoItemApi!!.getAllTodoItems()

            val body = response.body()

            if (body == null)
                emit(Result.failure(NullPointerException()))

            val todoList = body!!.list
            val revision = body.revision

            Result.success(
                TodoItemsResponse(
                    todoList.map { value -> value.toData() },
                    revision
                )
            )
        })
    }

    /**
     * Adds a todo item to the remote data source.
     *
     * @param todoItem The TodoItemData to be added.
     * @param revision The current revision number.
     * @param status The status of the todo item.
     * @return A result containing a TodoItemResponse.
     */
    override suspend fun addTodoItem(
        todoItem: ru.versoit.data.models.TodoItemData,
        revision: Int,
        status: String
    ): Result<TodoItemsResponse> = handleNetworkResult {
        val response = todoItemApi!!.addTodoItem(
            revision,
            TodoItemContainer(
                status = status,
                element = TodoItemDTO.fromData(todoItem),
                revision = revision
            )
        )

        val result = getResultFromResponse(response)
        result.map { last ->
            TodoItemsResponse(
                data = last.list.map { value -> value.toData() },
                lastRevision = last.revision
            )
        }
    }

    /**
     * Updates todo item.
     *
     * @param todoItem The updated todo item.
     * @param revision The current revision number.
     * @param status The status of the todo item.
     * @return A result containing TodoItemResponse.
     */
    override suspend fun updateTodoItem(
        todoItem: ru.versoit.data.models.TodoItemData,
        revision: Int,
        status: String
    ): Result<TodoItemResponse> = handleNetworkResult {

        val response = todoItemApi!!.updateTodoItem(
            revision,
            todoItem.id,
            TodoItemContainer(
                status = status, element = TodoItemDTO.fromData(todoItem),
                revision = revision
            )
        )

        val result = getResultFromResponse(response)

        result.map { last ->
            TodoItemResponse(
                data = last.element.toData(),
                lastRevision = last.revision
            )
        }
    }

    /**
     * Removes a todoItem.
     *
     * @param id The id of todo item to be removed.
     * @param revision The current revision number.
     * @return A result containing a TodoItemResponse.
     */
    override suspend fun removeTodoItem(id: String, revision: Int): Result<TodoItemResponse> =
        handleNetworkResult {
            val response = todoItemApi!!.removeTodoItem(revision, id)

            val body = getResultFromResponse(response)

            body.map { last ->
                TodoItemResponse(
                    data = last.element.toData(),
                    lastRevision = last.revision
                )
            }
        }

    /**
     * Updates all todo items.
     *
     * @param todoItems The list of TodoItemData to be updated.
     * @param status The status of the TodoItems.
     * @param revision The current revision number.
     * @return A Result containing the updated revision number.
     */
    override suspend fun updateAllTodoItems(
        todoItems: List<ru.versoit.data.models.TodoItemData>,
        status: String,
        revision: Int
    ): Result<Int> = handleNetworkResult {
        val actualRevision = todoItemApi!!.updateAllTodoItems(
            revision,
            TodoItemsContainer(
                status = status,
                list = todoItems.map { TodoItemDTO.fromData(it) },
                revision = revision
            )
        )

        Result.success(actualRevision.body()?.revision ?: 0)
    }

    private fun <T> getResultFromResponse(response: Response<T>): Result<T> {

        val body = response.body() ?: return Result.failure(NullPointerException())

        if (!response.isSuccessful)
            return Result.failure(SyncException())

        return Result.success(body)
    }

    /**
     * Handles network requests and captures any exceptions.
     *
     * @param action The action to be executed.
     * @return A Result containing the result of the action.
     */
    private suspend fun <T> handleNetworkResult(action: suspend () -> Result<T>): Result<T> {
        return try {
            action()
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}