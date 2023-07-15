package ru.versoit.data.repository

import ru.versoit.data.storage.datasources.local.ThemeDataSource
import ru.versoit.domain.models.ThemeType
import ru.versoit.domain.repository.ThemesRepository

class ThemesRepositoryImpl(private val themeDataSource: ThemeDataSource) : ThemesRepository {

    override suspend fun getCurrentTheme() = themeDataSource.getCurrentTheme()

    override suspend fun saveTheme(theme: ThemeType) = themeDataSource.saveTheme(theme)
}