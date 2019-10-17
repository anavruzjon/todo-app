package com.nakhmadov.todo_app.screens.tasks

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TasksViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(TasksViewModel::class.java))
            return TasksViewModel(application) as T

        throw IllegalArgumentException("Unknown ViewModel class")

    }

}