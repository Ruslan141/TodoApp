package ru.versoit.todoapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.versoit.domain.models.Importance
import ru.versoit.domain.models.TodoItem
import ru.versoit.domain.usecase.AddTodoItemUseCase
import ru.versoit.todoapp.utils.DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * ViewModel for creating new todo item.
 *
 * @param addTodoItemUseCase The use case for adding todo item into storage.
 *
 * @property deadline The deadline date of future element at flow.
 * @property importance The importance of the future element at flow.
 * @property text The text of the future element.
 * @property formattedDeadline The formatted deadline date.
 * @property isInvalidDeadline Deadline validity flag.
 * @property isInvalidText Text validity flag.
 * @property hasDeadline Deadline flag.
 * @property year Year of deadline.
 * @property month Month of deadline.
 * @property day Day of deadline.
 */

class NewTodoItemViewModel(private val addTodoItemUseCase: AddTodoItemUseCase) : ViewModel() {

    private val _deadline = MutableStateFlow(Date())
    val deadline: Flow<Date> = _deadline

    private val _importance = MutableStateFlow(Importance.UNIMPORTANT)
    val importance: Flow<Importance> = _importance

    var text = ""
        set(value) {
            field = value.trim().lowercase().replaceFirstChar { it.uppercase() }
        }

    /**
     * Updates importance of current todo item.
     */
    fun updateImportance(importance: Importance) {
        _importance.value = importance
    }

    val formattedDeadline: String
        get() = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(
            _deadline.value
        )

    val isInvalidDeadline: Boolean
        get() {
            val currentDate = getDateNow()
            return currentDate > _deadline.value
        }

    val isInvalidText: Boolean get() = text.isEmpty()

    var hasDeadline: Boolean = false

    /**
     * Saves new todo item to repository.
     *
     * This method delegates saving task to the [addTodoItemUseCase].
     */
    fun save() {

        val todoItem =
            TodoItem(
                id = "",
                text = text,
                importance = _importance.value,
                deadline = if (hasDeadline) _deadline.value else null,
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

    /**
     * Updates deadline.
     *
     * @param year The year of new deadline.
     * @param day The day of new deadline.
     * @param month The month of new deadline.
     */
    fun updateDeadline(day: Int, month: Int, year: Int) {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, year)

        _deadline.value = calendar.time
    }

    /**
     * Deadline year of current todo item.
     */
    val year: Int
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = _deadline.value
            return calendar.get(Calendar.YEAR)
        }

    /**
     * Deadline month of current todo item.
     */
    val month: Int
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = _deadline.value
            return calendar.get(Calendar.MONTH)
        }
    /**
     * Deadline day of current todo item.
     */
    val day: Int
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = _deadline.value
            return calendar.get(Calendar.DAY_OF_MONTH)
        }

    private fun getDateNow(): Date {
        val date = Date()
        val calendar = Calendar.getInstance()

        calendar.time = date

        calendar.set(Calendar.HOUR, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        return calendar.time
    }
}