package ru.versoit.todoapp.presentation.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.versoit.todoapp.databinding.LayoutTaskUnimportantBinding
import ru.versoit.todoapp.domain.models.TodoItem
import ru.versoit.todoapp.presentation.adapters.TodoItemsAdapter
import ru.versoit.todoapp.presentation.viewmodels.TodoItemUpdater

class UnimportantTodoItemViewHolder(
    private val binding: LayoutTaskUnimportantBinding,
    private val todoItemUpdater: TodoItemUpdater
) :
    RecyclerView.ViewHolder(binding.root), TodoItemsAdapter.ViewHolder {

    constructor(view: View, todoItemUpdater: TodoItemUpdater) : this(
        LayoutTaskUnimportantBinding.bind(view),
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
            todoItemUpdater.updateTodoItem(model)
        }
    }

    private fun setTextState(isChecked: Boolean) {
        if (!isChecked)
            binding.textViewText.animateRemoveStrikeThrough()
        else
            binding.textViewText.animateStrikeThrough()
    }
}