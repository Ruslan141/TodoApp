package ru.versoit.todoapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.versoit.todoapp.domain.models.Importance
import ru.versoit.todoapp.domain.models.TodoItem
import ru.versoit.todoapp.domain.usecase.AddTodoItemUseCase
import ru.versoit.todoapp.utils.DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class NewTodoItemViewModel(private val addTodoItemUseCase: AddTodoItemUseCase) : ViewModel() {

    private val _deadline = MutableStateFlow(Date())
    val deadline: Flow<Date> = _deadline

    private val _importance = MutableStateFlow(Importance.UNIMPORTANT)
    val importance: Flow<Importance> = _importance

    var text = ""
        set(value) {
            field = value.trim().lowercase().replaceFirstChar { it.uppercase() }
        }

    fun updateImportance(importance: Importance) {
        _importance.value = importance
    }

    val formattedDate: String
        get() = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(
            _deadline.value
        )

    val isInvalidDeadline: Boolean
        get() {
            val currentDate = getInitDate()
            return currentDate > _deadline.value
        }

    val isInvalidText: Boolean get() = text.isEmpty()

    var isDeadline: Boolean = false

    fun save() {

        val todoItem =
            TodoItem(
                id = "",
                text = text,
                importance = _importance.value,
                deadline = if (isDeadline) _deadline.value else null,
                done = false,
                lastUpdatedBy = "some_user",
                lastUpdate = Date(),
                created = Date(),
                color = null
            )

        viewModelScope.launch {
            addTodoItemUseCase(todoItem)
        }
    }

    fun updateDate(day: Int, month: Int, year: Int) {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, year)

        _deadline.value = calendar.time
    }

    val year: Int
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = _deadline.value
            return calendar.get(Calendar.YEAR)
        }

    val month: Int
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = _deadline.value
            return calendar.get(Calendar.MONTH)
        }

    val day: Int
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = _deadline.value
            return calendar.get(Calendar.DAY_OF_MONTH)
        }

    private fun getInitDate(): Date {
        val date = Date()
        val calendar = Calendar.getInstance()

        calendar.time = date

        calendar.set(Calendar.HOUR, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        return calendar.time
    }
}