package ru.versoit.todoapp.data.storage.datasources

interface TokenDataSource {

    suspend fun getValue(): String

    suspend fun save(value: String)

    suspend fun hasToken(): Boolean
}