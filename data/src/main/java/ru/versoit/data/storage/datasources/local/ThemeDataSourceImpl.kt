package ru.versoit.data.storage.datasources.local

import android.content.Context
import ru.versoit.domain.models.ThemeType

class ThemeDataSourceImpl(context: Context) : ThemeDataSource {

    companion object {
        private const val SHARED_PREFS_NAME = "THEME_SHARED_PREFS"
        private const val THEME_NAME = "THEME"
    }

    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    override suspend fun getCurrentTheme(): ThemeType =
        ThemeType.values()[sharedPrefs.getInt(THEME_NAME, 0)]

    override suspend fun saveTheme(theme: ThemeType) {
        sharedPrefs.edit().putInt(THEME_NAME, theme.ordinal).apply()
    }
}