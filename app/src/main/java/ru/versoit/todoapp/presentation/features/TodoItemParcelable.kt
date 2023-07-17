package ru.versoit.todoapp.presentation.features

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.versoit.domain.models.Importance
import java.util.Date

@Parcelize
data class TodoItemParcelable(
    var id: String,
    val text: String,
    val importance: Importance,
    val deadline: Date?,
    val done: Boolean,
    val color: String?,
    val created: Date,
    val lastUpdate: Date,
    val lastUpdatedBy: String
) : Parcelable