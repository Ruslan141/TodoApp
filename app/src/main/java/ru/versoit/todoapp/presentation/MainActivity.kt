package ru.versoit.todoapp.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.versoit.todoapp.R
import ru.versoit.todoapp.data.repository.TodoItemRepositoryImpl
import ru.versoit.todoapp.data.repository.TokenRepositoryImpl
import ru.versoit.todoapp.data.storage.datasources.RetrofitTodoItemDataSource
import ru.versoit.todoapp.data.storage.datasources.RoomTodoItemDataSource
import ru.versoit.todoapp.data.storage.datasources.SharedPrefsRevisionDataSource
import ru.versoit.todoapp.data.storage.datasources.TokenDataSourceImpl
import ru.versoit.todoapp.domain.repository.NetworkSynchronizer
import ru.versoit.todoapp.domain.usecase.GetTokenUseCase
import ru.versoit.todoapp.domain.usecase.SaveTokenUseCase
import ru.versoit.todoapp.presentation.features.NetworkChangeMonitor
import ru.versoit.todoapp.presentation.features.NetworkSyncWorker
import ru.versoit.todoapp.presentation.features.YandexAuth
import ru.versoit.todoapp.presentation.features.vmfactory.MainActivityViewModelFactory
import ru.versoit.todoapp.presentation.viewmodels.MainActivityViewModel


class MainActivity : AppCompatActivity() {

    private var networkChangeMonitor: NetworkChangeMonitor? = null

    private var networkSynchronizer: NetworkSynchronizer? = null

    private var yandexAuth: YandexAuth? = null

    private val viewModel by viewModels<MainActivityViewModel> {
        MainActivityViewModelFactory(
            GetTokenUseCase(TokenRepositoryImpl(TokenDataSourceImpl(this))),
            SaveTokenUseCase(TokenRepositoryImpl(TokenDataSourceImpl(this)))
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            startActivityForResult(auth.intent, YandexAuth.REQUEST_LOGIN_SDK)
        }
    }

    @Deprecated("Deprecated in Kotlin")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        CoroutineScope(Dispatchers.IO).launch {
            if (requestCode == YandexAuth.REQUEST_LOGIN_SDK) {
                try {
                    yandexAuth?.let { auth ->
                        val yandexAuthToken: YandexAuthToken? =
                            auth.sdk.extractToken(resultCode, data)
                        if (yandexAuthToken != null) {
                            viewModel.saveToken(yandexAuthToken.value)
                            Log.e("TOKEN_FOR_DEBUG", yandexAuthToken.value)
                            launch(Dispatchers.Main) {
                                getNavController().navigate(R.id.action_loadingFragment_to_tasksFragment)

                                initNetworkMonitoring()
                                startListeningNetwork()
                            }
                        } else {
                            finish()
                        }
                    }
                } catch (e: YandexAuthException) {
                    Toast.makeText(baseContext, R.string.try_again_later, Toast.LENGTH_LONG).show()
                }
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