package ru.versoit.data.storage.datasources.network

/**
 * The network synchronizations callbacks interface.
 *
 * @property onSyncSuccess Invokes on network synchronization success.
 * @property onSyncFailure Invokes on network synchronization failure.
 */
interface SyncCallback {

    var onSyncSuccess: suspend () -> Unit

    var onSyncFailure: suspend () -> Unit
}