package ru.versoit.todoapp.data.storage.datasources

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsRevisionDataSource(context: Context) : RevisionDataSource {

    private val sharedPrefs: SharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    override suspend fun getValue() = sharedPrefs.getInt(REVISION_NAME, DEFAULT)

    override suspend fun save(value: Int) {
        sharedPrefs.edit().putInt(REVISION_NAME, value).apply()
    }
}

private const val SHARED_PREFS_NAME = "LAST_REVISION"

private const val REVISION_NAME = "REVISION"

private const val DEFAULT = 0