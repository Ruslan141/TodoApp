package ru.versoit.todoapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.versoit.domain.models.Importance
import ru.versoit.domain.models.TodoItem
import ru.versoit.domain.usecase.TodoItemRemoveUseCase
import ru.versoit.domain.usecase.TodoItemUpdateUseCase
import ru.versoit.todoapp.utils.DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * ViewModel for editing todoItem.
 *
 * @param updateTodoItemUseCase The use case for updating todo item.
 * @param todoItemRemoveUseCase The use case for remove todo item.
 *
 * @property todoItem The current todo item.
 * @property isDeadline Checks is todo item has deadline.
 * @property text The text of deadline of current todo item.
 * @property deadline The deadline of current todo item.
 * @property importance The importance of current todo item.
 * @property lastChangedFormatted The formatted date of last current todo item change.
 * @property formattedDeadline The deadline formatted date of current todo item.
 * @property isInvalidDeadline Deadline validity flag.
 * @property isInvalidText Text validity flag.
 * @property deadlineYear The deadline year of current todo item.
 * @property deadlineMonth The deadline month of current todo item.
 * @property deadlineDay The deadline day of current todo item.
 */

class EditTodoItemViewModel(
    private val updateTodoItemUseCase: TodoItemUpdateUseCase,
    private val todoItemRemoveUseCase: TodoItemRemoveUseCase,
) : ViewModel(), ImportanceUpdater {

    private var _todoItem = MutableStateFlow<TodoItem?>(null)
    val todoItem: Flow<TodoItem?> = _todoItem

    private var todoItemToEdit: TodoItem? = null

    var isDeadline = false

    val text: String
        get() = todoItemToEdit?.text ?: ""

    val deadline: Flow<Date>
        get() = todoItem.map {
            it?.deadline!!
        }

    val importance: Flow<Importance>
        get() = todoItem.map { it?.importance ?: Importance.IMPORTANT }

    val lastChangedFormatted: Flow<String>
        get() = todoItem.map {
            SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(
                todoItemToEdit?.lastUpdate ?: Date()
            )
        }

    val deadlineYear: Int
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = todoItemToEdit?.deadline ?: calendar.time
            return calendar.get(Calendar.YEAR)
        }

    val deadlineMonth: Int
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = todoItemToEdit?.deadline ?: calendar.time
            return calendar.get(Calendar.MONTH)
        }

    val deadlineDay: Int
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = todoItemToEdit?.deadline ?: calendar.time
            return calendar.get(Calendar.DAY_OF_MONTH)
        }

    val formattedDeadline: String
        get() = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(
            todoItemToEdit?.deadline ?: Date()
        )

    val isInvalidDeadline: Boolean
        get() {
            val currentDate = getDateNow()
            if (todoItemToEdit?.deadline == null)
                return false

            return currentDate > todoItemToEdit?.deadline
        }

    val isInvalidText: Boolean get() = todoItemToEdit?.text?.isEmpty() ?: false

    /**
     * Updates text of current todo item.
     */

    fun updateText(text: String) {
        val newTodoItem =
            todoItemToEdit?.copy(text = text.trim().lowercase().replaceFirstChar { it.uppercase() })
        todoItemToEdit = newTodoItem
        _todoItem.value = todoItemToEdit
    }

    /**
     * Sets the current todo item to be edited.
     */
    fun setItemToEdit(todoItem: TodoItem) {
        isDeadline = todoItem.deadline != null

        todoItemToEdit = if (todoItem.deadline == null)
            todoItem.copy(deadline = Date())
        else
            todoItem

        _todoItem.value = todoItemToEdit
    }

    /**
     * Updates importance of current todo item.
     *
     * @param importance The selected importance of todo item.
     */
    override fun updateImportance(importance: Importance) {
        todoItemToEdit = todoItemToEdit?.copy(importance = importance)
        _todoItem.value = todoItemToEdit
    }

    /**
     * Updates deadline of current todo item.
     *
     * @param day The day of new deadline.
     * @param month The month of new deadline.
     * @param year The year of new deadline.
     */
    fun updateDeadline(day: Int, month: Int, year: Int) {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, year)

        todoItemToEdit = todoItemToEdit?.copy(deadline = calendar.time)
        _todoItem.value = todoItemToEdit
    }

    /**
     * Removes current todo item.
     *
     * This method delegates the task of removing to the [todoItemRemoveUseCase].
     */
    fun removeTodoItem() {

        viewModelScope.launch {
            todoItemToEdit?.let {
                todoItemRemoveUseCase(todoItemToEdit!!.id)
            }
        }
    }

    /**
     * Applies changes to the repository.
     *
     * This method delegates the task of updating to the [updateTodoItemUseCase].
     */
    fun update() {

        viewModelScope.launch {
            todoItemToEdit?.let {
                if (isDeadline)
                    updateTodoItemUseCase(it.copy(lastUpdate = Date()))
                else
                    updateTodoItemUseCase(it.copy(deadline = null, lastUpdate = Date()))
            }
        }
    }

    /**
     * Returns date with the current day, month and year, but zero hours, zero minutes and seconds.
     *
     * @return Date
     */
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