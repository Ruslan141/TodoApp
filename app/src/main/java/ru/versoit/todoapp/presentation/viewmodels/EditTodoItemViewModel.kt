package ru.versoit.todoapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.versoit.todoapp.domain.models.Importance
import ru.versoit.todoapp.domain.models.TodoItem
import ru.versoit.todoapp.domain.usecase.TodoItemRemoveUseCase
import ru.versoit.todoapp.domain.usecase.TodoItemUpdateUseCase
import ru.versoit.todoapp.utils.DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EditTodoItemViewModel(
    private val updateTodoItemUseCase: TodoItemUpdateUseCase,
    private val todoItemRemoveUseCase: TodoItemRemoveUseCase,
) : ViewModel() {

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

    fun updateText(text: String) {
        val newTodoItem =
            todoItemToEdit?.copy(text = text.trim().lowercase().replaceFirstChar { it.uppercase() })
        todoItemToEdit = newTodoItem
        _todoItem.value = todoItemToEdit
    }

    fun setItemToEdit(todoItem: TodoItem) {
        isDeadline = todoItem.deadline != null

        todoItemToEdit = if (todoItem.deadline == null)
            todoItem.copy(deadline = Date())
        else
            todoItem

        _todoItem.value = todoItemToEdit
    }

    fun updateImportance(importance: Importance) {
        todoItemToEdit = todoItemToEdit?.copy(importance = importance)
        _todoItem.value = todoItemToEdit
    }

    fun updateDeadline(day: Int, month: Int, year: Int) {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, year)

        todoItemToEdit = todoItemToEdit?.copy(deadline = calendar.time)
        _todoItem.value = todoItemToEdit
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

    fun removeTodoItem() {

        viewModelScope.launch {
            todoItemToEdit?.let {
                todoItemRemoveUseCase(todoItemToEdit!!.id)
            }
        }
    }

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

    val year: Int
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = todoItemToEdit?.deadline ?: calendar.time
            return calendar.get(Calendar.YEAR)
        }

    val month: Int
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = todoItemToEdit?.deadline ?: calendar.time
            return calendar.get(Calendar.MONTH)
        }

    val day: Int
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = todoItemToEdit?.deadline ?: calendar.time
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