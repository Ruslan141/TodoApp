package ru.versoit.data.storage.datasources.local

/**
 * A datasource retrieves and handles data of revision.
 */
interface RevisionDataSource {

    /**
     * Gets a value of current revision.
     *
     * @return Current revision.
     */
    suspend fun getValue(): Int

    /**
     * Saves a value of current revision.
     *
     * @param value Revision to save.
     */
    suspend fun save(value: Int)
}