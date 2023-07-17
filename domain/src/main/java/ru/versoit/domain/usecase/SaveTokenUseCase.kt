package ru.versoit.domain.usecase

import ru.versoit.domain.repository.TokenRepository

class SaveTokenUseCase(private val tokenRepository: TokenRepository) {

    suspend operator fun invoke(token: String) = tokenRepository.saveToken(token)
}