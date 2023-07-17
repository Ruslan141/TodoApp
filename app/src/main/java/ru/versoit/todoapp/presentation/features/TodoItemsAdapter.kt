package ru.versoit.todoapp.presentation.features

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.versoit.todoapp.R
import ru.versoit.domain.models.Importance
import ru.versoit.domain.models.TodoItem
import ru.versoit.todoapp.presentation.viewholders.ImportantTodoItemViewHolder
import ru.versoit.todoapp.presentation.viewholders.LessImportantTodoItemViewHolder
import ru.versoit.todoapp.presentation.viewholders.UnimportantTodoItemViewHolder
import ru.versoit.todoapp.presentation.viewmodels.TodoItemRemover
import ru.versoit.todoapp.presentation.viewmodels.TodoItemUpdater

/**
 * Adapter for displaying a todo list of items.
 *
 * @param todoItemUpdater The updater interface for handling todo item updates.
 * @param todoItemRemover The remover interface for handling todo item removal.
 * @param todoItemEditor The editor interface for handling todo item editing.
 */
class TodoItemsAdapter(
    private val todoItemUpdater: TodoItemUpdater,
    private val todoItemRemover: TodoItemRemover,
    private val todoItemEditor: TodoItemEditor,
) : ListAdapter<TodoItem, RecyclerView.ViewHolder>(ItemDiffCallback()) {

    /**
     * Creates a new ViewHolder by inflating the appropriate item view based on item's importance.
     *
     * @param parent The parent of ViewHolder
     * @param viewType The view type of the item.
     * @return The created RecyclerView.ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (ru.versoit.domain.models.Importance.values()[viewType]) {
            ru.versoit.domain.models.Importance.UNIMPORTANT ->
                getUnimportantTodoItemViewHolder(inflater, parent)

            ru.versoit.domain.models.Importance.LESS_IMPORTANT ->
                getLessImportantTodoItemViewHolder(inflater, parent)

            ru.versoit.domain.models.Importance.IMPORTANT ->
                getImportantTodoItemViewHolder(inflater, parent)
        }
    }

    private fun getUnimportantTodoItemViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): UnimportantTodoItemViewHolder {
        val itemView = inflater.inflate(R.layout.task_unimportant, parent, false)
        return UnimportantTodoItemViewHolder(
            itemView,
            todoItemUpdater,
            todoItemEditor,
            todoItemRemover
        )
    }

    private fun getImportantTodoItemViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ImportantTodoItemViewHolder {
        val itemView = inflater.inflate(R.layout.task_important, parent, false)
        return ImportantTodoItemViewHolder(
            itemView,
            todoItemUpdater,
            todoItemEditor,
            todoItemRemover
        )
    }

    private fun getLessImportantTodoItemViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): LessImportantTodoItemViewHolder {
        val itemView = inflater.inflate(R.layout.task_less_important, parent, false)
        return LessImportantTodoItemViewHolder(
            itemView,
            todoItemUpdater,
            todoItemEditor,
            todoItemRemover
        )
    }

    operator fun get(index: Int): TodoItem {
        return getItem(index)
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

    /**
     * Represents base ViewHolder.
     */
    interface ViewHolder {

        fun bind(model: TodoItem)
    }
}