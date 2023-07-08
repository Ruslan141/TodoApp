package ru.versoit.data.storage.datasources.local

/**
 * Represents a token data source.
 */
interface TokenDataSource {

    /**
     * Gets a last saved token.
     *
     * @return token
     */
    suspend fun getValue(): String

    /**
     * Saves a token.
     *
     * @param value Token to save.
     */
    suspend fun save(value: String)

    /**
     * Checks is token exists.
     *
     * @return `true` if token exists, `false` otherwise.
     */
    suspend fun hasToken(): Boolean
}