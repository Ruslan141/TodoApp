package ru.versoit.todoapp.data.storage.retrofit.dto

import com.google.gson.annotations.SerializedName
import ru.versoit.todoapp.data.models.TodoItemData
import ru.versoit.todoapp.data.models.importanceToRemote
import ru.versoit.todoapp.domain.models.Importance
import ru.versoit.todoapp.utils.toDate

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

        fun fromData(todoItem: TodoItemData): TodoItemDTO =
            TodoItemDTO(
                id = todoItem.id,
                text = todoItem.text,
                importance = importanceToRemote(todoItem.importance),
                deadline = todoItem.deadline?.time,
                done = todoItem.done,
                created = todoItem.created.time,
                color = todoItem.color,
                lastUpdate = todoItem.lastUpdate.time,
                lastUpdatedBy = todoItem.lastUpdatedBy
            )
    }
}

fun TodoItemDTO.toData() = TodoItemData(
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