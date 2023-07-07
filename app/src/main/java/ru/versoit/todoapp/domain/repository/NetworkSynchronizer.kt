package ru.versoit.todoapp.domain.repository

interface NetworkSynchronizer {

    suspend fun synchronizeWithNetwork(): Boolean
}