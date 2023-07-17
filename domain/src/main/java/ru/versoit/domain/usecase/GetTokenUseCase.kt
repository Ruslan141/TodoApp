package ru.versoit.domain.usecase

import ru.versoit.domain.repository.TokenRepository

class GetTokenUseCase(private val tokenRepository: TokenRepository) {

    suspend operator fun invoke(): String = tokenRepository.getToken()

    suspend fun hasToken(): Boolean = tokenRepository.hasToken()
}