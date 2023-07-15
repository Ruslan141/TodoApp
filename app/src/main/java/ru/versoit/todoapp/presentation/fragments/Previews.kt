package ru.versoit.todoapp.presentation.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.versoit.domain.models.Importance
import ru.versoit.todoapp.R
import ru.versoit.todoapp.presentation.AppTheme
import ru.versoit.todoapp.presentation.appDarkColors
import ru.versoit.todoapp.presentation.appLightColors

@Preview
@Composable
fun appPalettePreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        appThemeLight()
        appThemeDark()
    }
}

@Preview
@Composable
fun NewTodoItemInDark() {
    todoItemPreview(isLight = false)
}

@Preview
@Composable
fun NewTodoItemInLight() {
    todoItemPreview(isLight = true)
}

@Composable
fun todoItemPreview(isLight: Boolean) {
    val text by remember { mutableStateOf("") }
    var isImportanceMenuExpanded by remember { mutableStateOf(false) }

    AppTheme {
        val colors = if (isLight) appLightColors() else appDarkColors()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colors.background)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.cancel),
                        tint = colors.onBackground
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .height(IntrinsicSize.Min)
                        .width(IntrinsicSize.Min)
                ) {
                    Text(
                        text = stringResource(id = R.string.add_task).uppercase(),
                        modifier = Modifier
                            .clickable(
                                onClick = {},
                            ),
                        color = colors.primary,
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp)
                    .height(IntrinsicSize.Min)
            ) {
                Box(modifier = Modifier.padding(top = 30.dp, bottom = 30.dp)) {
                    TextField(value = text,
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = colors.surface,
                            unfocusedIndicatorColor = Color.Transparent,
                            textColor = colors.onBackground
                        ),
                        minLines = 4,
                        placeholder = {
                            Text(
                                text = stringResource(R.string.what_should_be_done),
                                color = colors.onSurface
                            )
                        })
                }
                importancePicker(
                    viewModel = null,
                    onImportanceTextClick = {
                        isImportanceMenuExpanded = !isImportanceMenuExpanded
                    },
                    isImportanceMenuExpanded = isImportanceMenuExpanded,
                    selectedImportance = Importance.IMPORTANT
                ) {
                    isImportanceMenuExpanded = false
                }
                Text(
                    text = stringResource(id = R.string.important),
                    color = colors.secondary,
                    modifier = Modifier.padding(top = 10.dp, bottom = 30.dp),
                    fontSize = 20.sp
                )
                Divider(
                    color = colors.secondary, thickness = 1.dp, modifier = Modifier.fillMaxWidth()
                )
                deadlinePicker(
                    isDeadline = true,
                    deadline = "03.12.2023",
                    onSwitchCheckedChange = {}) {}
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun deadlinePickerPreview() {
    AppTheme {
        deadlinePicker(isDeadline = true, deadline = "03.12.2023")
    }
}

@Preview(showBackground = true)
@Composable
fun importancePickerPreview() {
    AppTheme {
        deadlinePicker(isDeadline = true, deadline = "03.12.2023")
    }
}

@Composable
fun appThemeLight() {

    Column {
        Box(modifier = Modifier.padding(20.dp)) {
            Text(text = "Color palette - light", fontSize = 32.sp)
        }
        appTheme(isDark = false)
    }
}

@Composable
fun appThemeDark() {

    Column {
        Box(modifier = Modifier.padding(20.dp)) {
            Text(text = "Color palette - dark", fontSize = 32.sp)
        }
        appTheme(isDark = true)
    }
}

@Composable
fun appTheme(isDark: Boolean) {

    val colors = if (isDark) appDarkColors() else appLightColors()

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            namedColor("Primary", colors.primary)
            namedColor("Background", colors.background)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            namedColor("Secondary", colors.secondary)
            namedColor("OnSurface", colors.onSurface)
        }
    }
}

@Composable
fun namedColor(name: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color)
            .padding(10.dp)
            .wrapContentWidth()
    ) {
        Text(
            text = name,
            color = if (color == Color.White) Color.Black else Color.White,
            fontSize = 32.sp
        )
    }
}