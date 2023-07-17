package ru.versoit.data.repository

import ru.versoit.data.storage.datasources.local.TokenDataSource
import ru.versoit.domain.repository.TokenRepository

/**
 * Implementation of the TokenRepository interface that handles and retrieves data of user token.
 *
 * @param tokenDataSource The data source of token.
 */
class TokenRepositoryImpl(private val tokenDataSource: TokenDataSource) : TokenRepository {

    /**
     * Saves the token into [tokenDataSource]
     *
     * @param token Token value.
     */
    override suspend fun saveToken(token: String) = tokenDataSource.save(token)

    /**
     * Retrieves token from [tokenDataSource]
     *
     * @return token
     */
    override suspend fun getToken() = tokenDataSource.getValue()

    /**
     * Checks the token from [tokenDataSources]
     *
     * @return `true`, if token exists, `else` otherwise.
     */
    override suspend fun hasToken() = tokenDataSource.hasToken()
}