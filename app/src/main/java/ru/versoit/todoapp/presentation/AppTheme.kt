package ru.versoit.todoapp.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import ru.versoit.todoapp.R

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val colors =
        if (isSystemInDarkTheme())
            appDarkColors()
        else appLightColors()

    MaterialTheme(
        colors = colors,
        content = content
    )
}

@Composable
fun appLightColors(): Colors {
    return lightColors(
        primary = colorResource(id = R.color.primary),
        background = colorResource(id = R.color.light_background),
        onBackground = colorResource(id = R.color.text_color_for_light),
        onSurface = Color.Black.copy(alpha = 0.4f),
        secondary = colorResource(id = R.color.text_color_for_light_subhead)
    )
}

@Composable
fun appDarkColors(): Colors {
    return darkColors(
        primary = colorResource(id = R.color.primary),
        background = colorResource(id = R.color.night_background),
        onBackground = colorResource(id = R.color.text_color_for_night),
        onSurface = colorResource(id = R.color.text_color_for_night),
        secondary = colorResource(id = R.color.text_color_for_night_subhead)
    )
}