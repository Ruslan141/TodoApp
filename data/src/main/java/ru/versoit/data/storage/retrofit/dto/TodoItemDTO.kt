package ru.versoit.data.storage.retrofit.dto

import com.google.gson.annotations.SerializedName
import ru.versoit.data.toDate
import ru.versoit.domain.models.Importance

/**
 * Data class representing a TodoItem DTO (Data Transfer Object).
 *
 * @property id The ID of the TodoItem.
 * @property text The text description of the TodoItem.
 * @property importance The importance level of the TodoItem.
 * @property deadline The deadline of the TodoItem, nullable.
 * @property done Indicates whether the TodoItem is marked as done.
 * @property color The color associated with the TodoItem, nullable.
 * @property created The timestamp of when the TodoItem was created.
 * @property lastUpdate The timestamp of the last update to the TodoItem.
 * @property lastUpdatedBy The identifier of the user who last updated the TodoItem.
 */
data class TodoItemDTO(
    @SerializedName("id")
    var id: String,
    @SerializedName("text")
    val text: String,
    @SerializedName("importance")
    val importance: String,
    @SerializedName("deadline")
    val deadline: Long?,
    @SerializedName("done")
    val done: Boolean,
    @SerializedName("color")
    val color: String?,
    @SerializedName("created_at")
    val created: Long,
    @SerializedName("changed_at")
    val lastUpdate: Long,
    @SerializedName("last_updated_by")
    val lastUpdatedBy: String
) {
    companion object {

        fun fromData(todoItem: ru.versoit.data.models.TodoItemData): TodoItemDTO =
            TodoItemDTO(
                id = todoItem.id,
                text = todoItem.text,
                importance = ru.versoit.data.models.importanceToRemote(todoItem.importance),
                deadline = todoItem.deadline?.time,
                done = todoItem.done,
                created = todoItem.created.time,
                color = todoItem.color,
                lastUpdate = todoItem.lastUpdate.time,
                lastUpdatedBy = todoItem.lastUpdatedBy
            )
    }
}

fun TodoItemDTO.toData() = ru.versoit.data.models.TodoItemData(
    id = id,
    text = text,
    importance = importanceToData(importance),
    deadline = deadline?.toDate(),
    done = done,
    created = created.toDate(),
    color = color,
    lastUpdate = lastUpdate.toDate(),
    lastUpdatedBy = lastUpdatedBy
)

fun importanceToData(importance: String) =
    when (importance) {
        "important" -> Importance.IMPORTANT
        "basic" -> Importance.LESS_IMPORTANT
        else -> Importance.UNIMPORTANT
    }