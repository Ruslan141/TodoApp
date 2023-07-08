package ru.versoit.todoapp.presentation.features

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.versoit.data.repository.TodoItemRepositoryImpl
import ru.versoit.data.storage.datasources.local.RoomTodoItemDataSource
import ru.versoit.data.storage.datasources.local.SharedPrefsRevisionDataSource
import ru.versoit.data.storage.datasources.local.TokenDataSourceImpl
import ru.versoit.data.storage.datasources.network.NetworkSynchronizer
import ru.versoit.data.storage.datasources.network.RetrofitTodoItemDataSource
import java.time.Duration

class NetworkSyncWorker(
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    companion object {

        private const val NAME = "NETWORK_SYNC_WORKER"

        private const val BACKOFF_DELAY_MINUTES: Long = 2

        private const val HOURS_REPEAT: Long = 8

        fun scheduleSyncWork(context: Context) {

            val backoffPolicy = BackoffPolicy.LINEAR
            val backoffDelay = Duration.ofMinutes(BACKOFF_DELAY_MINUTES)

            val constraints = getConstraints()

            val request = getPeriodicWorkRequest(constraints, backoffPolicy, backoffDelay)

            enqueueWorkManager(context, request)
        }

        private fun getPeriodicWorkRequest(
            constraints: Constraints,
            backoffPolicy: BackoffPolicy,
            backoffDelay: Duration
        ): PeriodicWorkRequest {

            return PeriodicWorkRequestBuilder<NetworkSyncWorker>(
                Duration.ofHours(
                    HOURS_REPEAT
                )
            )
                .setConstraints(constraints)
                .setBackoffCriteria(backoffPolicy, backoffDelay)
                .build()
        }

        private fun getConstraints(): Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        private fun enqueueWorkManager(context: Context, request: PeriodicWorkRequest) {
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }

    private val synchronizer: NetworkSynchronizer = TodoItemRepositoryImpl(
        RoomTodoItemDataSource(context),
        RetrofitTodoItemDataSource(),
        SharedPrefsRevisionDataSource(context),
        TokenDataSourceImpl(context)
    )

    override suspend fun doWork() = withContext(Dispatchers.IO) {

        if (synchronizer.synchronizeWithNetwork()) {
            Result.success()
        }
        Result.failure()
    }
}