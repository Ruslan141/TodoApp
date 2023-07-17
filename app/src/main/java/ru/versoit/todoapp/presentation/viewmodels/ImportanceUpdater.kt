package ru.versoit.todoapp.presentation.viewmodels

import ru.versoit.domain.models.Importance

interface ImportanceUpdater {

    fun updateImportance(importance: Importance)
}