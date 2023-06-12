package ru.versoit.todoapp.presentation.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.versoit.todoapp.databinding.LayoutTaskImportantBinding
import ru.versoit.todoapp.domain.models.TodoItem
import ru.versoit.todoapp.presentation.adapters.TodoItemsAdapter

class ImportantTodoItemViewHolder(private val binding: LayoutTaskImportantBinding) :
    RecyclerView.ViewHolder(binding.root), TodoItemsAdapter.ViewHolder {

    constructor(view: View) : this(LayoutTaskImportantBinding.bind(view))

    override fun bind(model: TodoItem) {
        binding.textViewText.text = model.text
        binding.checkBoxState.isChecked = model.execution

        setTextState(!model.execution)

        itemView.setOnClickListener {
            setTextState(binding.checkBoxState.isChecked)
            Snackbar.make(itemView, model.id, Snackbar.LENGTH_LONG).show()
        }

        itemView.setOnClickListener {
            setTextState(binding.checkBoxState.isChecked)
            binding.checkBoxState.isChecked = !binding.checkBoxState.isChecked
            Snackbar.make(itemView, model.id, Snackbar.LENGTH_LONG).show()
        }

        binding.checkBoxState.setOnCheckedChangeListener { _, isChecked ->
            setTextState(!isChecked)
            Snackbar.make(itemView, model.id, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun setTextState(isChecked: Boolean) {
        if (isChecked)
            binding.textViewText.animateRemoveStrikeThrough()
        else
            binding.textViewText.animateStrikeThrough()

        binding.textViewText.alpha = if (isChecked) ACTIVE else NOT_ACTIVE
    }

    companion object {

        const val NOT_ACTIVE = 0.2f

        const val ACTIVE = 1f
    }
}