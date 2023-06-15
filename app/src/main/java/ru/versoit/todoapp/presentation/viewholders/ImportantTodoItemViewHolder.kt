package ru.versoit.todoapp.presentation.viewholders

import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ru.versoit.todoapp.R
import ru.versoit.todoapp.databinding.TaskImportantBinding
import ru.versoit.todoapp.domain.models.TodoItem
import ru.versoit.todoapp.presentation.adapters.TodoItemsAdapter
import ru.versoit.todoapp.presentation.fragments.TodoItemEditor
import ru.versoit.todoapp.presentation.viewmodels.TodoItemRemover
import ru.versoit.todoapp.presentation.viewmodels.TodoItemUpdater
import java.text.SimpleDateFormat
import java.util.Locale

class ImportantTodoItemViewHolder(
    private val binding: TaskImportantBinding,
    private val todoItemUpdater: TodoItemUpdater,
    private val todoItemEditor: TodoItemEditor,
    private val todoItemRemover: TodoItemRemover,
) :
    RecyclerView.ViewHolder(binding.root), TodoItemsAdapter.ViewHolder {

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

    override fun bind(model: TodoItem) {
        binding.textViewText.text = model.text
        binding.checkBoxState.isChecked = model.completed
        setTextState(model.completed)

        binding.checkBoxState.setOnClickListener {
            setTextState(binding.checkBoxState.isChecked)
            model.completed = binding.checkBoxState.isChecked
            todoItemUpdater.updateTodoItem(model)
        }

        if (model.isDeadline) {
            binding.textViewDeadline.visibility = View.VISIBLE
            binding.textViewDeadline.text =
                SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(model.deadline)
        } else {
            binding.textViewDeadline.visibility = View.GONE
        }

        itemView.setOnLongClickListener { it ->
            val popupMenu = PopupMenu(it.context, it)
            popupMenu.inflate(R.menu.menu_opens)
            popupMenu.show()

            popupMenu.setOnMenuItemClickListener {

                when (it.itemId) {
                    R.id.remove -> {
                        todoItemRemover.removeTodoItem(absoluteAdapterPosition)
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
        if (!isChecked)
            binding.textViewText.animateRemoveStrikeThrough()
        else
            binding.textViewText.animateStrikeThrough()
    }
}