package ru.versoit.todoapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import ru.versoit.todoapp.R
import ru.versoit.todoapp.data.repository.TodoItemRepositoryImpl
import ru.versoit.todoapp.data.storage.datasources.RetrofitTodoItemDataSource
import ru.versoit.todoapp.data.storage.datasources.RoomTodoItemDataSource
import ru.versoit.todoapp.data.storage.datasources.SharedPrefsRevisionDataSource
import ru.versoit.todoapp.domain.repository.NetworkSynchronizer
import ru.versoit.todoapp.presentation.features.NetworkChangeMonitor
import ru.versoit.todoapp.presentation.features.NetworkSyncWorker

class MainActivity : AppCompatActivity() {

    private var networkChangeMonitor: NetworkChangeMonitor? = null

    private var networkSynchronizer: NetworkSynchronizer? = null

    private val mainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNetworkMonitoring()
        startListeningNetwork()

        NetworkSyncWorker.scheduleSyncWork(this)
    }

    private fun initNetworkMonitoring() {
        networkChangeMonitor = NetworkChangeMonitor(this)
        networkSynchronizer = TodoItemRepositoryImpl(
            RoomTodoItemDataSource(this),
            RetrofitTodoItemDataSource(),
            SharedPrefsRevisionDataSource(this)
        )
    }

    private fun startListeningNetwork() {
        networkChangeMonitor?.startMonitoring()

        mainScope.launch(Dispatchers.IO) {

            while (true) {
                networkChangeMonitor?.connectivityState()?.collect { isConnected ->
                    if (isConnected) {
                        networkSynchronizer?.synchronizeWithNetwork()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }
}