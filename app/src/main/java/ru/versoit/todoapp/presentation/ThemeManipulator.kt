package ru.versoit.todoapp.presentation

import ru.versoit.domain.models.ThemeType

interface ThemeManipulator {

    suspend fun saveTheme(theme: ThemeType)

    suspend fun getCurrentTheme(): ThemeType
}