package ru.versoit.todoapp.presentation.features

import android.content.Intent
import androidx.navigation.NavController
import ru.versoit.todoapp.presentation.MainActivity.Companion.TODO_ITEM_PARCELABLE_NAME
import ru.versoit.todoapp.presentation.fragments.LoadingFragmentDirections
import ru.versoit.todoapp.presentation.fragments.TodoItemsFragmentDirections
import ru.versoit.todoapp.utils.toDomain

class IntentToTodoItemEditFragmentHandler(
    private val intent: Intent,
    private val navController: NavController
) {

    @Suppress("Deprecation")
    operator fun invoke(): Boolean {

        val todoItemToEdit =
            intent.getParcelableExtra(TODO_ITEM_PARCELABLE_NAME) as? TodoItemParcelable
        if (todoItemToEdit != null) {
            val action =
                LoadingFragmentDirections.actionLoadingFragmentToEditTodoItemFragment(todoItemToEdit.toDomain())
            navController.navigate(action)
            return true
        }

        return false
    }
}