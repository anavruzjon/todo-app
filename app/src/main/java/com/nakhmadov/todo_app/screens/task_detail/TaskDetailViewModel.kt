package com.nakhmadov.todo_app.screens.task_detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nakhmadov.todo_app.R
import com.nakhmadov.todo_app.data.Task
import com.nakhmadov.todo_app.data.TasksRepository
import com.nakhmadov.todo_app.data.db.getDatabase
import kotlinx.coroutines.*


class TaskDetailViewModel(id: Long, application: Application) : AndroidViewModel(application) {

    private val taskId = id
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val database = getDatabase(application.applicationContext)
    private val repository = TasksRepository.getInstance(database)
    private var task: Task
    private val context = application.applicationContext


    private val _eventStatusChange = MutableLiveData<String>()
    val eventStatusChange: LiveData<String>
        get() = _eventStatusChange

    init {
        task = getTask(taskId)
    }


    fun getTask(taskId: Long): Task {
        var task = Task()
        runBlocking {
            val job = async { repository.getTaskById(taskId) }
            task = job.await()
        }
        return task
    }

    private fun updateTask(task: Task) {
        uiScope.launch {
            repository.update(task)
        }
    }

    fun onCompleteStatusChanged() {
        val task = getTask(taskId)
        if (task.isCompleted) {
            task.isCompleted = false
            _eventStatusChange.value = context.getString(R.string.task_is_not_completed)
            updateTask(task)
        } else {
            task.isCompleted = true
            _eventStatusChange.value = context.getString(R.string.task_is_completed)
            updateTask(task)
        }
    }

    fun statusChanged() {
        _eventStatusChange.value = null
    }

    fun deleteTask(taskId: Long) {
        uiScope.launch {
            repository.deleteTask(taskId)
        }
    }


    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }


}