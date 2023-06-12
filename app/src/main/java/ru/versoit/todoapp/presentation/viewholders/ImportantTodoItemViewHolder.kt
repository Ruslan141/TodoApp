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

        itemView.setOnClickListener {
            if (binding.checkBoxState.isChecked) {
                binding.textViewText.animateRemoveStrikeThrough()
                binding.checkBoxState.isChecked = false
            }
            else {
                binding.textViewText.animateStrikeThrough()
                binding.checkBoxState.isChecked = true
            }
            Snackbar.make(itemView, model.id, Snackbar.LENGTH_LONG).show()
        }

        itemView.setOnClickListener {
            if (binding.checkBoxState.isChecked) {
                binding.textViewText.animateRemoveStrikeThrough()
                binding.checkBoxState.isChecked = false
            }
            else {
                binding.textViewText.animateStrikeThrough()
                binding.checkBoxState.isChecked = true
            }
            Snackbar.make(itemView, model.id, Snackbar.LENGTH_LONG).show()
        }

        binding.checkBoxState.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                binding.textViewText.animateRemoveStrikeThrough()
            }
            else {
                binding.textViewText.animateStrikeThrough()
            }
            Snackbar.make(itemView, model.id, Snackbar.LENGTH_LONG).show()
        }
    }
}