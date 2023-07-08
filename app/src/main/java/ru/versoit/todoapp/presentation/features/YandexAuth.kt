package ru.versoit.todoapp.presentation.features

import android.content.Context
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk

/**
 * Shell for yandex auth
 *
 * @property sdk Represents YandexAuth sdk
 * @property intent Represents login Intent
 */
class YandexAuth(context: Context) {

    private val loginOptionsBuilder = YandexAuthLoginOptions.Builder()

    val sdk = YandexAuthSdk(
        context, YandexAuthOptions(context)
    )

    val intent = sdk.createLoginIntent(loginOptionsBuilder.build())
}