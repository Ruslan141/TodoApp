package ru.versoit.todoapp.presentation.features.vmfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.versoit.domain.usecase.GetTokenUseCase
import ru.versoit.domain.usecase.ManipulateThemesUseCase
import ru.versoit.domain.usecase.SaveTokenUseCase
import ru.versoit.todoapp.presentation.viewmodels.MainViewModel

class MainActivityViewModelFactory(
    private val getTokenUseCase: GetTokenUseCase,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val manipulateThemesUseCase: ManipulateThemesUseCase
) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        MainViewModel(
            getTokenUseCase,
            saveTokenUseCase,
            manipulateThemesUseCase
        ) as T
}