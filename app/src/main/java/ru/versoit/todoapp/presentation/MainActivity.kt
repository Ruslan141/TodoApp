package ru.versoit.todoapp.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.versoit.data.repository.TodoItemRepositoryImpl
import ru.versoit.data.storage.datasources.local.RoomTodoItemDataSource
import ru.versoit.data.storage.datasources.local.SharedPrefsRevisionDataSource
import ru.versoit.data.storage.datasources.local.TokenDataSourceImpl
import ru.versoit.data.storage.datasources.network.NetworkSynchronizer
import ru.versoit.data.storage.datasources.network.RetrofitTodoItemDataSource
import ru.versoit.todoapp.R
import ru.versoit.todoapp.app.TodoApp
import ru.versoit.todoapp.presentation.features.NetworkChangeMonitor
import ru.versoit.todoapp.presentation.features.NetworkSyncWorker
import ru.versoit.todoapp.presentation.features.YandexAuth
import ru.versoit.todoapp.presentation.features.vmfactory.MainActivityViewModelFactory
import ru.versoit.todoapp.presentation.viewmodels.MainViewModel
import javax.inject.Inject

/**
 * This class represents the main activity of the TodoApp.
 *
 * @property networkChangeMonitor The network change monitor instance.
 * @property networkSynchronizer The network synchronizer instance.
 * @property yandexAuth The YandexAuth instance.
 * @property viewModel The view model for the main activity.
 */

class MainActivity : AppCompatActivity() {

    private val tag = "MainActivity"

    private var networkChangeMonitor: NetworkChangeMonitor? = null

    private var networkSynchronizer: NetworkSynchronizer? = null

    private var yandexAuth: YandexAuth? = null

    @Inject
    lateinit var viewModelFactory: MainActivityViewModelFactory
    private lateinit var viewModel: MainViewModel

    private val yandexAuthContract =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            CoroutineScope(Dispatchers.IO).launch {
                if (result.resultCode == Activity.RESULT_OK) {
                    try {
                        tryAuth(result.resultCode, result.data)
                    } catch (exception: YandexAuthException) {
                        Log.e(tag, exception.message.toString())
                        Toast.makeText(baseContext, R.string.try_again_later, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }

    private fun inject() {
        (applicationContext as TodoApp).appComponent.mainActivityComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inject()

        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        lifecycleScope.launch(Dispatchers.IO) {
            if (!viewModel.hasToken())
                authorize()
            else {
                initNetworkMonitoring()
                startListeningNetwork()

                NetworkSyncWorker.scheduleSyncWork(this@MainActivity)

                launch(Dispatchers.Main) {
                    getNavController().navigate(R.id.action_loadingFragment_to_tasksFragment)
                }
            }
        }
    }

    private fun getNavController(): NavController {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView_content) as NavHostFragment

        return navHostFragment.navController
    }

    private fun authorize() {
        yandexAuth = YandexAuth(this)
        yandexAuth?.let { auth ->
            yandexAuthContract.launch(auth.intent)
        }
    }

    private suspend fun tryAuth(resultCode: Int, data: Intent?) {
        yandexAuth?.let { auth ->
            val yandexAuthToken: YandexAuthToken? =
                auth.sdk.extractToken(resultCode, data)
            if (yandexAuthToken != null) {
                viewModel.saveToken(yandexAuthToken.value)
                withContext(Dispatchers.Main) {
                    getNavController().navigate(R.id.action_loadingFragment_to_tasksFragment)

                    initNetworkMonitoring()
                    startListeningNetwork()
                }
            } else {
                finish()
            }
        }
    }

    private fun startListeningNetwork() {
        networkChangeMonitor?.startMonitoring()

        lifecycleScope.launch(Dispatchers.IO) {

            networkChangeMonitor?.connectivityState()?.collect { isConnected ->
                if (isConnected) {
                    networkSynchronizer?.synchronizeWithNetwork()
                }
            }
        }
    }

    private fun initNetworkMonitoring() {
        networkChangeMonitor = NetworkChangeMonitor(this)
        networkSynchronizer = TodoItemRepositoryImpl(
            RoomTodoItemDataSource(this),
            RetrofitTodoItemDataSource(),
            SharedPrefsRevisionDataSource(this),
            TokenDataSourceImpl(this)
        )
    }
}