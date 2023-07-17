package ru.versoit.todoapp.presentation.features

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.versoit.domain.models.ThemeType
import ru.versoit.todoapp.presentation.ThemeManipulator

class ThemeStatusChanger(
    private val themeManipulator: ThemeManipulator
) : ThemeChanger {

    override fun setDarkTheme() {
        CoroutineScope(Dispatchers.IO).launch {
            themeManipulator.saveTheme(ThemeType.NIGHT)
        }
    }

    override fun setLightTheme() {
        CoroutineScope(Dispatchers.IO).launch {
            themeManipulator.saveTheme(ThemeType.LIGHT)
        }
    }

    override fun setSystemTheme() {
        CoroutineScope(Dispatchers.IO).launch {
            themeManipulator.saveTheme(ThemeType.SYSTEM)
        }
    }
}