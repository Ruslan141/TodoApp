package ru.versoit.todoapp.data.storage.datasources

interface RevisionDataSource {

    suspend fun getValue(): Int

    suspend fun save(value: Int)
}