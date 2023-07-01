package ru.versoit.todoapp.presentation.features.vmfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.versoit.todoapp.domain.usecase.GetTokenUseCase
import ru.versoit.todoapp.domain.usecase.SaveTokenUseCase
import ru.versoit.todoapp.presentation.viewmodels.MainActivityViewModel

class MainActivityViewModelFactory(
    private val getTokenUseCase: GetTokenUseCase,
    private val saveTokenUseCase: SaveTokenUseCase
) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        MainActivityViewModel(
            getTokenUseCase,
            saveTokenUseCase
        ) as T
}