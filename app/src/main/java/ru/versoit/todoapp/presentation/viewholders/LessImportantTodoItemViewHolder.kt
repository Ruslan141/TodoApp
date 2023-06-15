package ru.versoit.todoapp.presentation.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.versoit.todoapp.databinding.TaskLessImportantBinding
import ru.versoit.todoapp.domain.models.TodoItem
import ru.versoit.todoapp.presentation.adapters.TodoItemsAdapter
import ru.versoit.todoapp.presentation.viewmodels.TodoItemUpdater
import java.text.SimpleDateFormat
import java.util.Locale

class LessImportantTodoItemViewHolder(
    private val binding: TaskLessImportantBinding,
    private val todoItemUpdater: TodoItemUpdater
) :
    RecyclerView.ViewHolder(binding.root), TodoItemsAdapter.ViewHolder {

    constructor(view: View, todoItemUpdater: TodoItemUpdater) : this(
        TaskLessImportantBinding.bind(view),
        todoItemUpdater
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
    }

    private fun setTextState(isChecked: Boolean) {
        if (!isChecked)
            binding.textViewText.animateRemoveStrikeThrough()
        else
            binding.textViewText.animateStrikeThrough()
    }
}