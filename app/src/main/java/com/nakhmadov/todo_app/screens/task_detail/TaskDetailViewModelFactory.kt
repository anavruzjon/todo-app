package com.nakhmadov.todo_app.screens.task_detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TaskDetailViewModelFactory(
    private val taskId: Long,
    private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskDetailViewModel::class.java)) {
            return TaskDetailViewModel(taskId, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
