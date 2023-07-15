package ru.versoit.todoapp.presentation.features.dialogs

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import ru.versoit.domain.models.ThemeType
import ru.versoit.todoapp.R
import ru.versoit.todoapp.databinding.AppThemeSelectionBottomSheetBinding
import ru.versoit.todoapp.presentation.features.ThemeChanger
import ru.versoit.todoapp.presentation.viewmodels.TodoItemsViewModel

class AppThemeBottomSheetSelection(
    context: Context,
    inflater: LayoutInflater,
    private val themeChanger: ThemeChanger,
    private val viewModel: TodoItemsViewModel,
    private val activity: Activity
) {

    private val bottomSheetDialog = BottomSheetDialog(context)

    private val binding = AppThemeSelectionBottomSheetBinding.inflate(inflater)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val selectedTheme = viewModel.getCurrentTheme()
            CoroutineScope(Dispatchers.Main).launch {
                when(selectedTheme) {
                    ThemeType.LIGHT -> binding.lightThemeRadioButton.isChecked = true
                    ThemeType.NIGHT -> binding.nightThemeRadioButton.isChecked = true
                    ThemeType.SYSTEM -> binding.systemThemeRadioButton.isChecked = true
                }
                binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
                    when(checkedId) {
                        R.id.light_theme_radio_button -> {
                            themeChanger.setLightTheme()
                        }
                        R.id.night_theme_radio_button -> {
                            themeChanger.setDarkTheme()
                        }
                        R.id.system_theme_radio_button -> {
                            themeChanger.setSystemTheme()
                        }
                    }
                    bottomSheetDialog.dismiss()
                    activity.recreate()
                }
            }
        }
    }

    private fun reload() {
        activity.recreate()
    }

    init {
        bottomSheetDialog.setContentView(binding.root)
    }

    fun show() {
        bottomSheetDialog.show()
    }
}