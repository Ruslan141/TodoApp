package ru.versoit.data.storage.datasources.network

interface NetworkSynchronizer {

    suspend fun synchronizeWithNetwork(): Boolean
}