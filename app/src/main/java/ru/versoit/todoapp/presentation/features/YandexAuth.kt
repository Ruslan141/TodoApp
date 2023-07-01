package ru.versoit.todoapp.presentation.features

import android.content.Context
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk

class YandexAuth(context: Context) {

    companion object {
        const val REQUEST_LOGIN_SDK = 1
    }

    private val loginOptionsBuilder = YandexAuthLoginOptions.Builder()

    val sdk = YandexAuthSdk(
        context, YandexAuthOptions(context)
    )

    val intent = sdk.createLoginIntent(loginOptionsBuilder.build())
}