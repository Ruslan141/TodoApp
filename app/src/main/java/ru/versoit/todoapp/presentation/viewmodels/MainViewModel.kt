package ru.versoit.todoapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import ru.versoit.domain.usecase.GetTokenUseCase
import ru.versoit.domain.usecase.SaveTokenUseCase

/**
 * ViewModel class for
 *
 * @param getTokenUseCase The use case for retrieving the token.
 * @param saveTokenUseCase The use case for saving the token.
 */

class MainViewModel(
    private val getTokenUseCase: GetTokenUseCase,
    private val saveTokenUseCase: SaveTokenUseCase
) : ViewModel() {

    /**
     * Saves the token.
     *
     * This method delegates the task of saving the token to the [saveTokenUseCase]
     *
     * @param token The token to be saved.
     */

    suspend fun saveToken(token: String) =
        saveTokenUseCase(token)

    /**
     * Checks if a token exists.
     *
     * This method delegates the task of checking if a token exists to the [getTokenUseCase].
     *
     * @return `true` if token exists, `false` otherwise.
     */

    suspend fun hasToken() = getTokenUseCase.hasToken()
}