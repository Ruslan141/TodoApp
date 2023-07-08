package ru.versoit.data.storage.datasources.local

import android.content.Context
import android.content.SharedPreferences

/**
 * Implementation of RevisionDataSource that handles and retrieves revision data from local storage.
 *
 * @param context
 */
class SharedPrefsRevisionDataSource(context: Context) : RevisionDataSource {

    companion object {
        private const val SHARED_PREFS_NAME = "LAST_REVISION"

        private const val REVISION_NAME = "REVISION"

        private const val DEFAULT = 0
    }

    private val sharedPrefs: SharedPreferences = context.getSharedPreferences(
        SHARED_PREFS_NAME,
        Context.MODE_PRIVATE
    )

    /**
     * Gets a value of current revision.
     *
     * @return Current revision.
     */
    override suspend fun getValue() = sharedPrefs.getInt(
        REVISION_NAME,
        DEFAULT
    )

    /**
     * Saves a value of current revision.
     *
     * @param value Revision to save.
     */
    override suspend fun save(value: Int) {
        sharedPrefs.edit().putInt(REVISION_NAME, value).apply()
    }
}