package ru.versoit.todoapp.presentation.features

import android.content.Context
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.versoit.todoapp.data.repository.TodoItemRepositoryImpl
import ru.versoit.todoapp.data.storage.datasources.RetrofitTodoItemDataSource
import ru.versoit.todoapp.data.storage.datasources.RoomTodoItemDataSource
import ru.versoit.todoapp.data.storage.datasources.SharedPrefsRevisionDataSource
import ru.versoit.todoapp.domain.repository.NetworkSynchronizer
import java.time.Duration

class NetworkSyncWorker(
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    private val synchronizer: NetworkSynchronizer = TodoItemRepositoryImpl(
        RoomTodoItemDataSource(context),
        RetrofitTodoItemDataSource(),
        SharedPrefsRevisionDataSource(context)
    )

    companion object {

        private const val NAME = "NETWORK_SYNC_WORKER"

        private const val BACKOFF_DELAY_MINUTES: Long = 2

        private const val HOURS_REPEAT: Long = 8

        fun scheduleSyncWork(context: Context) {

            val backoffPolicy = BackoffPolicy.LINEAR
            val backoffDelay = Duration.ofMinutes(BACKOFF_DELAY_MINUTES)

            val constraints = getConstraints()

            val periodicWorkRequest = PeriodicWorkRequestBuilder<NetworkSyncWorker>(
                Duration.ofHours(
                    HOURS_REPEAT
                )
            )
                .setConstraints(constraints)
                .setBackoffCriteria(backoffPolicy, backoffDelay)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWorkRequest)
        }

        private fun getConstraints(): Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    }

    override suspend fun doWork() = withContext(Dispatchers.IO) {
        Log.e("WORKER", "WORKER_NETWORK_SYNCHRONIZER")
        if (synchronizer.synchronizeWithNetwork()) {
            Result.success()
        }
        Result.failure()
    }
}