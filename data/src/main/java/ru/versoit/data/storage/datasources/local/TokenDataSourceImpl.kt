package ru.versoit.data.storage.datasources.local

import android.content.Context
import android.content.SharedPreferences

/**
 * Implementation of TokenDataSource
 *
 * @param context
 */
class TokenDataSourceImpl(context: Context) : TokenDataSource {

    private val sharedPrefs: SharedPreferences = context.getSharedPreferences(
        TOKEN_SHARED_PREFS,
        Context.MODE_PRIVATE
    )

    /**
     * Gets a last saved token.
     *
     * @return token
     */
    override suspend fun getValue() = sharedPrefs.getString(TOKEN_NAME, DEFAULT)!!

    /**
     * Saves a token.
     *
     * @param value Token to save.
     */
    override suspend fun save(value: String) {
        sharedPrefs.edit().putString(TOKEN_NAME, value).apply()
    }

    /**
     * Checks is token exists.
     *
     * @return `true` if token exists, `false` otherwise.
     */
    override suspend fun hasToken() = sharedPrefs.getString(TOKEN_NAME, DEFAULT)?.isNotEmpty()!!
}

private const val TOKEN_SHARED_PREFS = "TOKEN_SHARED_PREFS"

private const val TOKEN_NAME = "TOKEN"

private const val DEFAULT = ""