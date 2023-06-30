package ru.versoit.todoapp.domain.repository

interface InternetFailure {

    var onInternetFailure: suspend () -> Unit
}