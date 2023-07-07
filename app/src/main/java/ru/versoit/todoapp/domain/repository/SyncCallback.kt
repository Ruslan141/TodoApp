package ru.versoit.todoapp.domain.repository

interface SyncCallback {

    var onSyncSuccess: suspend () -> Unit

    var onSyncFailure: suspend () -> Unit
}