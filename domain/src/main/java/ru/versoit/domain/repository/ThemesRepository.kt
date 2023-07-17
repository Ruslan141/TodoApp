package ru.versoit.domain.repository

import ru.versoit.domain.models.ThemeType

interface ThemesRepository {

    suspend fun getCurrentTheme(): ThemeType

    suspend fun saveTheme(theme: ThemeType)
}