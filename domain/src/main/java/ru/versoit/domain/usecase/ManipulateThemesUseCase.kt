package ru.versoit.domain.usecase

import ru.versoit.domain.models.ThemeType
import ru.versoit.domain.repository.ThemesRepository

class ManipulateThemesUseCase(private val themesRepository: ThemesRepository) {

    suspend fun getCurrentTheme(): ThemeType = themesRepository.getCurrentTheme()

    suspend fun saveTheme(theme: ThemeType) = themesRepository.saveTheme(theme)
}