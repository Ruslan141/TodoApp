package ru.versoit.todoapp.presentation.features

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.versoit.todoapp.R
import ru.versoit.todoapp.domain.models.Importance
import ru.versoit.todoapp.domain.models.TodoItem
import ru.versoit.todoapp.presentation.viewholders.ImportantTodoItemViewHolder
import ru.versoit.todoapp.presentation.viewholders.LessImportantTodoItemViewHolder
import ru.versoit.todoapp.presentation.viewholders.UnimportantTodoItemViewHolder
import ru.versoit.todoapp.presentation.viewmodels.TodoItemRemover
import ru.versoit.todoapp.presentation.viewmodels.TodoItemUpdater

class TodoItemsAdapter(
    private val todoItemUpdater: TodoItemUpdater,
    private val todoItemRemover: TodoItemRemover,
    private val todoItemEditor: TodoItemEditor,
) : ListAdapter<TodoItem, RecyclerView.ViewHolder>(ItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (Importance.values()[viewType]) {

            Importance.UNIMPORTANT -> {
                val itemView = inflater.inflate(R.layout.task_unimportant, parent, false)
                UnimportantTodoItemViewHolder(
                    itemView,
                    todoItemUpdater,
                    todoItemRemover,
                    todoItemEditor
                )
            }

            Importance.LESS_IMPORTANT -> {
                val itemView = inflater.inflate(R.layout.task_less_important, parent, false)
                LessImportantTodoItemViewHolder(
                    itemView,
                    todoItemUpdater,
                    todoItemRemover,
                    todoItemEditor
                )
            }

            Importance.IMPORTANT -> {
                val itemView = inflater.inflate(R.layout.task_important, parent, false)
                ImportantTodoItemViewHolder(
                    itemView,
                    todoItemUpdater,
                    todoItemEditor,
                    todoItemRemover
                )
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.hashCode().toLong()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder)
            holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).importance.ordinal
    }

    private class ItemDiffCallback : DiffUtil.ItemCallback<TodoItem>() {

        override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem) = oldItem == newItem
    }

    interface ViewHolder {

        fun bind(model: TodoItem)
    }
}