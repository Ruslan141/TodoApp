package ru.versoit.todoapp.data.storage.retrofit

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.versoit.todoapp.data.storage.retrofit.dto.TodoItemContainer
import ru.versoit.todoapp.data.storage.retrofit.dto.TodoItemsContainer

interface TaskApiService {

    companion object {

        const val LAST_KNOWN_REVISION_HEADER = "X-Last-Known-Revision"
    }

    @GET("list/{id}")
    suspend fun getTodoItem(@Path("id") id: String): Response<TodoItemContainer>

    @GET("list")
    suspend fun getAllTodoItems(): Response<TodoItemsContainer>

    @PATCH("list")
    suspend fun updateAllTodoItems(
        @Header(LAST_KNOWN_REVISION_HEADER) lastKnownRevision: Int,
        @Body todoItems: TodoItemsContainer
    ): Response<TodoItemsContainer>

    @DELETE("list/{id}")
    suspend fun removeTodoItem(
        @Header(LAST_KNOWN_REVISION_HEADER) lastKnownRevision: Int,
        @Path("id") id: String
    ): Response<TodoItemContainer>

    @POST("list")
    suspend fun addTodoItem(
        @Header(LAST_KNOWN_REVISION_HEADER) lastKnownRevision: Int,
        @Body todoItem: TodoItemContainer
    ): Response<TodoItemsContainer>

    @PUT("list/{id}")
    suspend fun updateTodoItem(
        @Header(LAST_KNOWN_REVISION_HEADER) lastKnownRevision: Int,
        @Path("id") id: String,
        @Body todoItem: TodoItemContainer
    ): Response<TodoItemContainer>
}