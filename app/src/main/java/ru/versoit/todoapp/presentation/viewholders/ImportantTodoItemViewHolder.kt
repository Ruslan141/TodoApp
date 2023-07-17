package ru.versoit.todoapp.presentation.viewholders

import android.view.View
import android.widget.CheckBox
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ru.versoit.todoapp.R
import ru.versoit.todoapp.databinding.TaskImportantBinding
import ru.versoit.domain.models.TodoItem
import ru.versoit.todoapp.presentation.features.TodoItemsAdapter
import ru.versoit.todoapp.presentation.features.TodoItemEditor
import ru.versoit.todoapp.presentation.viewmodels.TodoItemRemover
import ru.versoit.todoapp.presentation.viewmodels.TodoItemUpdater
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * ViewHolder class for displaying important todo items.
 *
 * @param binding The view binding for the view holder.
 * @param todoItemUpdater The interface for updating todo item.
 * @param todoItemEditor The interface for editing todo item.
 * @param todoItemRemover The interface for removing todo item.
 */
class ImportantTodoItemViewHolder(
    private val binding: TaskImportantBinding,
    private val todoItemUpdater: TodoItemUpdater,
    private val todoItemEditor: TodoItemEditor,
    private val todoItemRemover: TodoItemRemover,
) :
    RecyclerView.ViewHolder(binding.root), TodoItemsAdapter.ViewHolder {

    /**
     * Secondary constructor for create view holder.
     *
     * @param view The root view of the ViewHolder.
     * @param todoItemUpdater The interface for updating todo item.
     * @param todoItemEditor The interface for editing todo item.
     * @param todoItemRemover The interface for removing todo item.
     */
    constructor(
        view: View,
        todoItemUpdater: TodoItemUpdater,
        todoItemEditor: TodoItemEditor,
        todoItemRemover: TodoItemRemover
    ) : this(
        TaskImportantBinding.bind(view),
        todoItemUpdater,
        todoItemEditor,
        todoItemRemover
    ) {
        binding.textViewText.isAlphaAnimate = true
    }

    /**
     * Binds the todo item data to the ViewHolder.
     *
     * @param model The todo item to be bound.
     */
    override fun bind(model: ru.versoit.domain.models.TodoItem) {

        bindUIByModel(model)
        showMenuOnLongListener(itemView, model)

        itemView.setOnClickListener {
            todoItemEditor.edit(model)
        }
    }

    private fun bindUIByModel(model: ru.versoit.domain.models.TodoItem) {

        with(binding) {
            textViewText.text = model.text
            checkBoxState.isChecked = model.done
            setTextState(model.done)

            bindCheckBoxTodoItemUpdaterListener(binding.checkBoxState, model)

            if (model.deadline != null) {
                textViewDeadline.visibility = View.VISIBLE
                textViewDeadline.text =
                    SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(model.deadline)
            } else {
                textViewDeadline.visibility = View.GONE
            }
        }
    }

    private fun bindCheckBoxTodoItemUpdaterListener(checkBoxState: CheckBox, model: ru.versoit.domain.models.TodoItem) {
        checkBoxState.setOnClickListener {
            setTextState(checkBoxState.isChecked)
            todoItemUpdater.updateTodoItem(
                model.copy(
                    done = checkBoxState.isChecked,
                    lastUpdate = Date()
                )
            )
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

    private fun showMenuOnLongListener(view: View, model: ru.versoit.domain.models.TodoItem) {
        view.setOnLongClickListener { value ->
            val popupMenu = PopupMenu(value.context, value)
            popupMenu.inflate(R.menu.menu_opens)
            setCrudEventsMenu(popupMenu, model)
            popupMenu.show()
            false
        }
    }

    private fun setCrudEventsMenu(popupMenu: PopupMenu, model: ru.versoit.domain.models.TodoItem) {

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
    }
}