package ru.versoit.data.storage.datasources.local

import ru.versoit.domain.models.ThemeType

interface ThemeDataSource {

    suspend fun getCurrentTheme(): ThemeType

    suspend fun saveTheme(theme: ThemeType)
}