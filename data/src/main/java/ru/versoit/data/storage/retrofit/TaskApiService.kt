package ru.versoit.data.storage.retrofit

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.versoit.data.storage.retrofit.dto.TodoItemContainer
import ru.versoit.data.storage.retrofit.dto.TodoItemsContainer

/**
 * Retrofit interface for task API service.
 */
interface TaskApiService {

    companion object {

        const val LAST_KNOWN_REVISION_HEADER = "X-Last-Known-Revision"
    }

    /**
     * Retrieves todo item by id.
     *
     * @param id Id of todo item.
     * @return Response containing a TodoItemContainer.
     */
    @GET("list/{id}")
    suspend fun getTodoItem(@Path("id") id: String): Response<TodoItemContainer>

    /**
     * Retrieves all todo items.
     *
     * @return A response containing all todo items.
     */
    @GET("list")
    suspend fun getAllTodoItems(): Response<TodoItemsContainer>

    /**
     * Updates all todo items.
     *
     * @param lastKnownRevision The last known revision.
     * @param todoItems The todo item container containing updated todo items.
     * @return A response containing a TodoItemsContainer.
     */
    @PATCH("list")
    suspend fun updateAllTodoItems(
        @Header(LAST_KNOWN_REVISION_HEADER) lastKnownRevision: Int,
        @Body todoItems: TodoItemsContainer
    ): Response<TodoItemsContainer>

    /**
     * Removes todo item by specified id.
     *
     * @param lastKnownRevision The last known revision number.
     * @param id The id of the todo item to remove.
     * @return A response containing a todo item container.
     */
    @DELETE("list/{id}")
    suspend fun removeTodoItem(
        @Header(LAST_KNOWN_REVISION_HEADER) lastKnownRevision: Int,
        @Path("id") id: String
    ): Response<TodoItemContainer>

    /**
     * Adds a new todo item.
     *
     * @param lastKnownRevision The last known revision.
     * @param todoItem The TodoItemContainer containing new todo item.
     * @return A response containing a TodoItemsContainer.
     */
    @POST("list")
    suspend fun addTodoItem(
        @Header(LAST_KNOWN_REVISION_HEADER) lastKnownRevision: Int,
        @Body todoItem: TodoItemContainer
    ): Response<TodoItemsContainer>

    /**
     * Updates a specific todo item.
     *
     * @param lastKnownRevision The last known revision number.
     * @param id The id of the todo item to update.
     * @param todoItem The updated TodoItemContainer.
     * @return A response containing a TodoItemContainer.
     */
    @PUT("list/{id}")
    suspend fun updateTodoItem(
        @Header(LAST_KNOWN_REVISION_HEADER) lastKnownRevision: Int,
        @Path("id") id: String,
        @Body todoItem: TodoItemContainer
    ): Response<TodoItemContainer>
}