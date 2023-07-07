package ru.versoit.todoapp.domain.usecase

import ru.versoit.todoapp.domain.repository.TokenRepository

class GetTokenUseCase(private val tokenRepository: TokenRepository) {

    suspend operator fun invoke(): String = tokenRepository.getToken()

    suspend fun hasToken(): Boolean = tokenRepository.hasToken()
}