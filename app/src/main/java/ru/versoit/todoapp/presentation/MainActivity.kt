package ru.versoit.todoapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.versoit.todoapp.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}