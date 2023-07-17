package ru.versoit.todoapp.presentation.fragments

import android.app.DatePickerDialog
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ru.versoit.domain.models.Importance
import ru.versoit.todoapp.R
import ru.versoit.todoapp.presentation.AppTheme
import ru.versoit.todoapp.presentation.viewmodels.NewTodoItemViewModel

@Composable
fun NewTodoItemScreen(navController: NavController) {
    val viewModel: NewTodoItemViewModel = viewModel()
    var text by remember { mutableStateOf("") }
    var isDeadline by remember { mutableStateOf(viewModel.hasDeadline) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var isImportanceMenuExpanded by remember { mutableStateOf(false) }
    val selectedImportance by viewModel.importance.collectAsState(Importance.UNIMPORTANT)
    var formattedDeadline by remember { mutableStateOf(viewModel.formattedDeadline) }

    AppTheme {
        val colors = MaterialTheme.colors
        Column(modifier = Modifier.fillMaxSize().background(color = colors.background)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { navController.navigateUp() }) {
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
                                onClick = {
                                    if (viewModel.isInvalidDeadline) {
                                        coroutineScope.launch {
                                            Toast.makeText(
                                                context,
                                                R.string.invalid_date,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                        return@clickable
                                    }

                                    if (viewModel.isInvalidText) {
                                        coroutineScope.launch {
                                            Toast.makeText(
                                                context,
                                                R.string.invalid_text,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                        return@clickable
                                    }

                                    viewModel.save()
                                    navController.navigateUp()
                                },
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
                        onValueChange = { newText ->
                            run {
                                text = newText
                                viewModel.text = text
                            }
                        },
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
                    viewModel = viewModel,
                    onImportanceTextClick = {
                        isImportanceMenuExpanded = !isImportanceMenuExpanded
                    },
                    isImportanceMenuExpanded = isImportanceMenuExpanded,
                    selectedImportance = selectedImportance
                ) {
                    isImportanceMenuExpanded = false
                }
                Divider(
                    color = colors.secondary, thickness = 1.dp, modifier = Modifier.fillMaxWidth()
                )
                deadlinePicker(
                    isDeadline = isDeadline,
                    deadline = formattedDeadline,
                    onSwitchCheckedChange = { newState ->
                        isDeadline = newState
                        viewModel.hasDeadline = newState
                    }) {
                    val datePicker =
                        DatePickerDialog(context, R.style.DatePicker)
                    datePicker.updateDate(
                        viewModel.year,
                        viewModel.month,
                        viewModel.day
                    )
                    datePicker.setOnDateSetListener { _, year, month, dayOfMonth ->
                        viewModel.updateDeadline(dayOfMonth, month, year)
                        formattedDeadline = viewModel.formattedDeadline
                    }
                    datePicker.show()
                }
            }
        }
    }
}

@Composable
fun importancePicker(
    viewModel: NewTodoItemViewModel?,
    isImportanceMenuExpanded: Boolean,
    onImportanceTextClick: () -> Unit = {},
    selectedImportance: Importance,
    onDismiss: () -> Unit = {}
) {
    val colors = MaterialTheme.colors
    Text(
        text = stringResource(id = R.string.importance),
        fontSize = 20.sp,
        color = colors.onBackground,
        modifier = Modifier.clickable {
            onImportanceTextClick()
        }
    )
    Box {
        DropdownMenu(
            expanded = isImportanceMenuExpanded,
            modifier = Modifier.background(colors.surface),
            onDismissRequest = { onDismiss() }) {
            importanceDropdownMenuItemByImportanceType(
                importance = Importance.UNIMPORTANT,
            ) {
                onDismiss()
                viewModel?.updateImportance(Importance.UNIMPORTANT)
            }
            importanceDropdownMenuItemByImportanceType(
                importance = Importance.LESS_IMPORTANT,
            ) {
                onDismiss()
                viewModel?.updateImportance(Importance.LESS_IMPORTANT)
            }
            importanceDropdownMenuItemByImportanceType(
                importance = Importance.IMPORTANT,
            ) {
                onDismiss()
                viewModel?.updateImportance(Importance.IMPORTANT)
            }
        }
    }
    Text(
        text = when (selectedImportance) {
            Importance.UNIMPORTANT -> stringResource(id = R.string.no)
            Importance.IMPORTANT -> stringResource(id = R.string.important)
            Importance.LESS_IMPORTANT -> stringResource(id = R.string.less_important_task)
        },
        color = colors.secondary,
        modifier = Modifier.padding(top = 10.dp, bottom = 30.dp).clickable { onImportanceTextClick() },
        fontSize = 20.sp
    )
}

@Composable
fun importanceDropdownMenuItemByImportanceType(
    importance: Importance,
    onClick: () -> Unit = {},
) {
    when (importance) {
        Importance.UNIMPORTANT -> defaultImportanceDropdownItem(
            text = R.string.no,
            onClick = onClick
        )

        Importance.LESS_IMPORTANT -> defaultImportanceDropdownItem(
            text = R.string.less_important_task,
            onClick = onClick
        )

        Importance.IMPORTANT -> importantImportanceDropdownMenuItem(
            text = R.string.important,
            onClick = onClick
        )
    }
}

@Composable
fun deadlinePicker(
    isDeadline: Boolean,
    deadline: String,
    onSwitchCheckedChange: (Boolean) -> Unit = {},
    onSelectedDateClick: () -> Unit = {}
) {
    val colors = MaterialTheme.colors
    Row(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.make_up),
            fontSize = 20.sp,
            color = colors.onBackground
        )
        Switch(
            checked = isDeadline,
            onCheckedChange = onSwitchCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = colors.primary)
        )
    }
    if (isDeadline) {
        Text(
            text = deadline,
            color = colors.primary,
            fontSize = 20.sp,
            modifier = Modifier.clickable(
                onClick = onSelectedDateClick
            )
        )
    }
}

@Composable
fun importantImportanceDropdownMenuItem(
    text: Int,
    onClick: () -> Unit = {},
) {
    val colors = MaterialTheme.colors
    DropdownMenuItem(
        onClick = onClick,
        modifier = Modifier.background(colors.surface)
    ) {
        Row {
            Text(
                text = "!! ",
                color = colorResource(id = R.color.important_attention_text),
                fontSize = 18.sp
            )
            Text(
                text = stringResource(id = text),
                color = colorResource(id = R.color.important_attention_text),
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun defaultImportanceDropdownItem(
    text: Int,
    onClick: () -> Unit = {}
) {
    val colors = MaterialTheme.colors
    DropdownMenuItem(
        onClick = onClick,
        modifier = Modifier.background(colors.surface)
    ) {
        Text(text = stringResource(id = text), color = colors.onBackground, fontSize = 18.sp)
    }
}