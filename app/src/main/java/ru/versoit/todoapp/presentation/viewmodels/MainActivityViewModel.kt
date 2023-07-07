package ru.versoit.todoapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import ru.versoit.todoapp.domain.usecase.GetTokenUseCase
import ru.versoit.todoapp.domain.usecase.SaveTokenUseCase

class MainActivityViewModel(
    private val getTokenUseCase: GetTokenUseCase,
    private val saveTokenUseCase: SaveTokenUseCase
) : ViewModel() {

    suspend fun saveToken(token: String) =
        saveTokenUseCase(token)

    suspend fun hasToken() = getTokenUseCase.hasToken()
}