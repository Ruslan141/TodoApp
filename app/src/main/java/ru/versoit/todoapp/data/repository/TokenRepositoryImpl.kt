package ru.versoit.todoapp.data.repository

import ru.versoit.todoapp.data.storage.datasources.TokenDataSource
import ru.versoit.todoapp.domain.repository.TokenRepository

class TokenRepositoryImpl(private val tokenDataSource: TokenDataSource) : TokenRepository {

    override suspend fun saveToken(token: String) = tokenDataSource.save(token)

    override suspend fun getToken() = tokenDataSource.getValue()

    override suspend fun hasToken() = tokenDataSource.hasToken()
}