package ru.versoit.domain.repository

interface TokenRepository {

    suspend fun saveToken(token: String)

    suspend fun getToken(): String

    suspend fun hasToken(): Boolean
}