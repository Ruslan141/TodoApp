package ru.versoit.todoapp.app

import android.app.Application
import ru.versoit.todoapp.di.component.AppComponent
import ru.versoit.todoapp.di.component.DaggerAppComponent
import ru.versoit.todoapp.di.component.MainActivityComponent
import ru.versoit.todoapp.di.module.AppModule

class TodoApp : Application() {

    lateinit var appComponent: AppComponent
    lateinit var mainActivityComponent: MainActivityComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(context = this))
            .build()
    }
}