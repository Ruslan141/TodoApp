package ru.versoit.data.storage.datasources.network

/**
 * The callback-interface of internet connection error.
 */
interface InternetFailure {

    var onInternetFailure: suspend () -> Unit
}