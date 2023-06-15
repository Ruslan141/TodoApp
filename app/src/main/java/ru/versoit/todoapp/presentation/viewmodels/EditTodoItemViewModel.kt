package ru.versoit.todoapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import ru.versoit.todoapp.domain.models.Importance
import ru.versoit.todoapp.domain.models.TodoItem
import ru.versoit.todoapp.domain.usecase.TodoItemRemoveUseCase
import ru.versoit.todoapp.domain.usecase.TodoItemUpdateUseCase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

const val DATE_FORMAT = "d MMM yyyy"

class EditTodoItemViewModel(
    private val updateTodoItemUseCase: TodoItemUpdateUseCase,
    private val todoItemRemoveUseCase: TodoItemRemoveUseCase
) : ViewModel() {

    private var _todoItem = MutableLiveData<TodoItem>()
    val todoItem: LiveData<TodoItem> = _todoItem

    private lateinit var todoItemToEdit: TodoItem

    val isDeadline
        get() = todoItemToEdit.isDeadline

    val text: String
        get() = todoItemToEdit.text

    val deadline: LiveData<Date>
        get() = todoItem.map { it.deadline }

    val importance: LiveData<Importance>
        get() = todoItem.map { it.importance }

    val lastChangedFormatted: LiveData<String>
        get() = todoItem.map {
            SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(
                todoItemToEdit.lastChanged
            )
        }

    fun updateText(text: String) {
        todoItemToEdit.text = text.trim().lowercase().replaceFirstChar { it.uppercase() }
        _todoItem.value = todoItemToEdit
    }

    fun setItemToEdit(todoItem: TodoItem) {
        todoItemToEdit = todoItem
        _todoItem.value = todoItem
    }

    fun updateImportance(importance: Importance) {
        todoItemToEdit.importance = importance
        _todoItem.value = todoItemToEdit
    }

    fun updateIsDeadline(isDeadline: Boolean) {
        todoItemToEdit.isDeadline = isDeadline
        _todoItem.value = todoItemToEdit
    }

    fun updateDeadline(day: Int, month: Int, year: Int) {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, year)

        todoItemToEdit.deadline = calendar.time
        _todoItem.value = todoItemToEdit
    }

    val formattedDeadline: String
        get() = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(
            todoItemToEdit.deadline
        )

    val isInvalidDeadline: Boolean
        get() {
            val currentDate = getDateNow()
            return currentDate > todoItemToEdit.deadline
        }

    val isInvalidText: Boolean get() = todoItemToEdit.text.isEmpty()

    fun removeTodoItem() {
        todoItemRemoveUseCase.removeTodoItem(todoItemToEdit.id)
    }

    fun update() {
        todoItemToEdit.lastChanged = Date()
        updateTodoItemUseCase.updateTodoItem(todoItemToEdit)
    }

    val year: Int
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = todoItemToEdit.deadline
            return calendar.get(Calendar.YEAR)
        }

    val month: Int
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = todoItemToEdit.deadline
            return calendar.get(Calendar.MONTH)
        }

    val day: Int
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = todoItemToEdit.deadline
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