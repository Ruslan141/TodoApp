package ru.versoit.todoapp.presentation.fragments

import ru.versoit.domain.models.Importance
import ru.versoit.todoapp.R
import ru.versoit.todoapp.presentation.viewmodels.ImportanceUpdater

class ImportanceSelector(private val importanceUpdater: ImportanceUpdater) {

    fun selectImportance(menuItemId: Int): Boolean {
        return when (menuItemId) {
            R.id.unimportant -> {
                importanceUpdater.updateImportance(Importance.UNIMPORTANT)
                true
            }

            R.id.less_important -> {
                importanceUpdater.updateImportance(Importance.LESS_IMPORTANT)
                true
            }

            R.id.important -> {
                importanceUpdater.updateImportance(Importance.IMPORTANT)
                true
            }

            else -> false
        }
    }
}