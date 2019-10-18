package com.nakhmadov.todo_app.screens.add_edit_task

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AddEditTaskViewModelFactory(private val taskId: Long,
                                  private val application: Application): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditTaskViewModel::class.java)) {
            return AddEditTaskViewModel(taskId, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}