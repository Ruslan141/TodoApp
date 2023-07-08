package ru.versoit.todoapp.presentation.fragments

import android.content.Context
import android.graphics.Canvas
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import ru.versoit.todoapp.R
import ru.versoit.todoapp.presentation.features.TodoItemsAdapter
import ru.versoit.todoapp.presentation.viewmodels.TodoItemCompleter
import ru.versoit.todoapp.presentation.viewmodels.TodoItemRemover
import ru.versoit.todoapp.presentation.viewmodels.UndoDeleter

class RecyclerViewSwiper(
    private val recyclerView: RecyclerView,
    private val adapter: TodoItemsAdapter,
    private val context: Context,
    private val todoItemsRemover: TodoItemRemover,
    private val undoDeleter: UndoDeleter,
    private val todoItemCompleter: TodoItemCompleter
) {

    private var vibrator: Vibrator? = null


    private fun handleSwipeDirection(
        direction: Int, recyclerView: RecyclerView, adapter: TodoItemsAdapter, position: Int
    ) {
        when (direction) {
            ItemTouchHelper.LEFT -> {
                todoItemsRemover.removeTodoItem(adapter[position])
                performVibration()
                Snackbar.make(
                    recyclerView,
                    R.string.removed_todo_item,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.undo) {
                        undoDeleter.undoDeletedTodoItem()
                    }.show()
            }

            ItemTouchHelper.RIGHT -> {
                if (!adapter[position].done) {
                    todoItemCompleter.setCompletedTodoItem(adapter[position])
                    performVibration()
                }
            }
        }
    }

    private fun createSwipeCallback(
        recyclerView: RecyclerView,
        adapter: TodoItemsAdapter
    ): ItemTouchHelper.SimpleCallback {
        return object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                handleSwipeDirection(direction, recyclerView, adapter, position)
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
            ) {
                val position = viewHolder.absoluteAdapterPosition
                if (position >= 0 && dX > 0 && adapter[position].done) {
                    return
                }
                customizeSwipeDecorator(
                    RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
                    )
                )
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
                )
            }
        }
    }

    private fun customizeSwipeDecorator(swipeDecorator: RecyclerViewSwipeDecorator.Builder) {

        swipeDecorator.addSwipeLeftBackgroundColor(ContextCompat.getColor(context, R.color.important_attention_text)).addSwipeRightBackgroundColor(
            ContextCompat.getColor(context, R.color.activated)).addSwipeLeftActionIcon(R.drawable.ic_remove_white).addSwipeRightActionIcon(
            R.drawable.ic_completed).create()
            .decorate()
    }

    fun attachSwipesToRecyclerView() {
        val simpleCallback = createSwipeCallback(recyclerView, adapter)

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun performVibration() {
        val vibrationDuration: Long = VIBRATION_DURATION

        vibrator?.let {

            if (it.hasVibrator()) {
                it.vibrate(
                    VibrationEffect.createOneShot(
                        vibrationDuration,
                        VibrationEffect.EFFECT_HEAVY_CLICK
                    )
                )
            }
        }
    }

    companion object {

        private const val VIBRATION_DURATION = 100L
    }
}