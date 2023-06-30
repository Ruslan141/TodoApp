package ru.versoit.todoapp.data.storage.datasources

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.versoit.todoapp.data.models.TodoItemData
import ru.versoit.todoapp.data.storage.retrofit.TaskApiService
import ru.versoit.todoapp.data.storage.retrofit.dto.TodoItemContainer
import ru.versoit.todoapp.data.storage.retrofit.dto.TodoItemDTO
import ru.versoit.todoapp.data.storage.retrofit.dto.TodoItemsContainer
import ru.versoit.todoapp.data.storage.retrofit.dto.toData
import java.io.IOException

class RetrofitTodoItemDataSource : RemoteTodoItemDataSource {

    companion object {

        const val BASE_URL: String = "https://beta.mrdekk.ru/todobackend/"

        const val TOKEN = "overmultiplication"

        const val AUTHORIZATION = "Authorization"

        const val BEARER = "Bearer"

        const val REVISION_FAILURE = 400
    }

    private val interceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader(AUTHORIZATION, "$BEARER $TOKEN")
            .build()

        chain.proceed(request)
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private val todoItemApi = retrofit.create(TaskApiService::class.java)

    override suspend fun getAllTodoItems(): Flow<TodoItemsResponse> = flow {
        val response = todoItemApi.getAllTodoItems()

        val body = handleResponseFailures(response)

        val todoList = body.list
        emit(TodoItemsResponse(todoList.map { value -> value.toData() }, body.revision))
    }

    override suspend fun addTodoItem(
        todoItem: TodoItemData,
        revision: Int,
        status: String
    ): TodoItemsResponse {

        val response = todoItemApi.addTodoItem(
            revision,
            TodoItemContainer(
                status = status,
                element = TodoItemDTO.fromData(todoItem),
                revision = revision
            )
        )

        val body = handleResponseFailures(response)

        return TodoItemsResponse(
            data = body.list.map { it.toData() },
            lastRevision = body.revision
        )
    }

    override suspend fun updateTodoItem(
        todoItem: TodoItemData,
        revision: Int,
        status: String
    ): TodoItemResponse {
        val response = todoItemApi.updateTodoItem(
            revision,
            todoItem.id,
            TodoItemContainer(
                status = status, element = TodoItemDTO.fromData(todoItem),
                revision = revision
            )
        )

        val body = handleResponseFailures(response)

        return TodoItemResponse(
            data = body.element.toData(),
            lastRevision = body.revision
        )
    }

    override suspend fun removeTodoItem(id: String, revision: Int): TodoItemResponse {
        val response = todoItemApi.removeTodoItem(revision, id)

        val body = handleResponseFailures(response)

        return TodoItemResponse(data = body.element.toData(), lastRevision = body.revision)
    }

    override suspend fun getTodoItemById(id: String): Flow<TodoItemResponse> = flow {
        val response = todoItemApi.getTodoItem(id)

        if (response.isSuccessful) {
            val todoItem = response.body()
            todoItem?.let {
                emit(TodoItemResponse(it.element.toData(), it.revision))
            }
        }
    }

    override suspend fun updateAllTodoItems(
        todoItems: List<TodoItemData>,
        status: String,
        revision: Int
    ): Int {
        val actualRevision = todoItemApi.updateAllTodoItems(
            revision,
            TodoItemsContainer(
                status = status,
                list = todoItems.map { TodoItemDTO.fromData(it) },
                revision = revision
            )
        )

        return actualRevision.body()?.revision ?: 0
    }

    private fun <T> handleResponseFailures(response: Response<T>): T {

        if (response.code() == REVISION_FAILURE)
            throw DifferentRevisionsException()

        if (response.body() == null)
            throw IOException()

        return response.body()!!
    }
}