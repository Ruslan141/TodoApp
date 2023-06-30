package ru.versoit.todoapp.presentation.viewholders

import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ru.versoit.todoapp.R
import ru.versoit.todoapp.databinding.TaskLessImportantBinding
import ru.versoit.todoapp.domain.models.TodoItem
import ru.versoit.todoapp.presentation.features.TodoItemsAdapter
import ru.versoit.todoapp.presentation.features.TodoItemEditor
import ru.versoit.todoapp.presentation.viewmodels.TodoItemRemover
import ru.versoit.todoapp.presentation.viewmodels.TodoItemUpdater
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LessImportantTodoItemViewHolder(
    private val binding: TaskLessImportantBinding,
    private val todoItemUpdater: TodoItemUpdater,
    private val todoItemRemover: TodoItemRemover,
    private val todoItemEditor: TodoItemEditor
) :
    RecyclerView.ViewHolder(binding.root), TodoItemsAdapter.ViewHolder {

    constructor(
        view: View,
        todoItemUpdater: TodoItemUpdater,
        todoItemRemover: TodoItemRemover,
        todoItemEditor: TodoItemEditor
    ) : this(
        TaskLessImportantBinding.bind(view),
        todoItemUpdater,
        todoItemRemover,
        todoItemEditor
    ) {
        binding.textViewText.isAlphaAnimate = true
    }

    override fun bind(model: TodoItem) {

        with(binding) {
            textViewText.text = model.text
            checkBoxState.isChecked = model.done
            setTextState(model.done)

            checkBoxState.setOnClickListener {
                setTextState(checkBoxState.isChecked)
                todoItemUpdater.updateTodoItem(model.copy(done = checkBoxState.isChecked, lastUpdate = Date()))
            }

            if (model.deadline != null) {
                textViewDeadline.visibility = View.VISIBLE
                textViewDeadline.text =
                    SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(model.deadline)
            } else {
                textViewDeadline.visibility = View.GONE
            }
        }

        itemView.setOnLongClickListener { it ->
            val popupMenu = PopupMenu(it.context, it)
            popupMenu.inflate(R.menu.menu_opens)
            popupMenu.show()

            popupMenu.setOnMenuItemClickListener {

                when (it.itemId) {
                    R.id.remove -> {
                        todoItemRemover.removeTodoItem(model)
                        true
                    }

                    R.id.edit -> {
                        todoItemEditor.edit(model)
                        true
                    }

                    else -> true
                }
            }

            false
        }

        itemView.setOnClickListener {
            todoItemEditor.edit(model)
        }
    }

    private fun setTextState(isChecked: Boolean) {
        with(binding) {
            if (!isChecked)
                textViewText.animateRemoveStrikeThrough()
            else
                textViewText.animateStrikeThrough()
        }
    }
}