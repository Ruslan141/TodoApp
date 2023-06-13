package ru.versoit.todoapp.presentation.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.versoit.todoapp.databinding.LayoutTaskImportantBinding
import ru.versoit.todoapp.domain.models.TodoItem
import ru.versoit.todoapp.presentation.adapters.TodoItemsAdapter
import ru.versoit.todoapp.presentation.viewmodels.TodoItemUpdater

class ImportantTodoItemViewHolder(
    private val binding: LayoutTaskImportantBinding,
    private val todoItemStateUpdater: TodoItemUpdater
) :
    RecyclerView.ViewHolder(binding.root), TodoItemsAdapter.ViewHolder {

    constructor(view: View, todoItemUpdater: TodoItemUpdater) : this(
        LayoutTaskImportantBinding.bind(view),
        todoItemUpdater
    ) {
        binding.textViewText.isAlphaAnimate = true
    }

    override fun bind(model: TodoItem) {
        binding.textViewText.text = model.text
        binding.checkBoxState.isChecked = model.state

        setTextState(model.state)

        itemView.setOnClickListener {
            setTextState(!binding.checkBoxState.isChecked)
            binding.checkBoxState.isChecked = !binding.checkBoxState.isChecked
            model.state = binding.checkBoxState.isChecked
            todoItemStateUpdater.updateTodoItem(model)
        }
    }

    private fun setTextState(isChecked: Boolean) {
        if (!isChecked)
            binding.textViewText.animateRemoveStrikeThrough()
        else
            binding.textViewText.animateStrikeThrough()
    }
}