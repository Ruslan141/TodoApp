package ru.versoit.todoapp.data.storage.datasources

import android.content.Context
import android.content.SharedPreferences

class TokenDataSourceImpl(context: Context) : TokenDataSource {

    private val sharedPrefs: SharedPreferences = context.getSharedPreferences(TOKEN_SHARED_PREFS, Context.MODE_PRIVATE)

    override suspend fun getValue() = sharedPrefs.getString(TOKEN_NAME, DEFAULT)!!

    override suspend fun save(value: String) {
        sharedPrefs.edit().putString(TOKEN_NAME, value).apply()
    }

    override suspend fun hasToken() = sharedPrefs.getString(TOKEN_NAME, DEFAULT)?.isNotEmpty()!!
}

private const val TOKEN_SHARED_PREFS = "TOKEN_SHARED_PREFS"

private const val TOKEN_NAME = "TOKEN"

private const val DEFAULT = ""