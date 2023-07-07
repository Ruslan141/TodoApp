package ru.versoit.todoapp.domain.usecase

import ru.versoit.todoapp.domain.repository.TokenRepository

class SaveTokenUseCase(private val tokenRepository: TokenRepository) {

    suspend operator fun invoke(token: String) = tokenRepository.saveToken(token)
}