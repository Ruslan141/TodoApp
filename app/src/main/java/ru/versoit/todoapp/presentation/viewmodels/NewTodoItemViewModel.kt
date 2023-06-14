package ru.versoit.todoapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.versoit.todoapp.domain.models.Importance
import ru.versoit.todoapp.domain.models.TodoItem
import ru.versoit.todoapp.domain.usecase.AddTodoItemUseCase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class NewTodoItemViewModel(private val addTodoItemUseCase: AddTodoItemUseCase) : ViewModel() {

    private var _currentDate = MutableLiveData(Date())
    val currentDate: LiveData<Date> = _currentDate

    private var _importance = MutableLiveData(Importance.UNIMPORTANT)
    var importance: LiveData<Importance> = _importance

    var text = ""

    fun updateImportance(importance: Importance) {
        _importance.value = importance
    }

    val formattedDate: String
        get() = SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(
            _currentDate.value!!
        )

    val isInvalidDate: Boolean
        get() {
            val currentDate = getInitDate()
            return currentDate > _currentDate.value
        }

    val isInvalidText: Boolean get() = text.isEmpty()

    fun save() {

        val todoItem =
            TodoItem("", text, importance.value!!, currentDate.value!!, false, Date(), Date())

        addTodoItemUseCase.addTodoItem(todoItem)
    }

    fun updateDate(day: Int, month: Int, year: Int) {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, year)

        _currentDate.value = calendar.time
    }

    val year: Int
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = currentDate.value!!
            return calendar.get(Calendar.YEAR)
        }

    val month: Int
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = currentDate.value!!
            return calendar.get(Calendar.MONTH)
        }

    val day: Int
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = currentDate.value!!
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