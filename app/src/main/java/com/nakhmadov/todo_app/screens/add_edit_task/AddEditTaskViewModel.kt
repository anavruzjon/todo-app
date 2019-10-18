package com.nakhmadov.todo_app.screens.add_edit_task

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


class AddEditTaskViewModel(id: Long, application: Application) : AndroidViewModel(application) {

    private val taskId = id
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val context = application.applicationContext
    private val database = getDatabase(application)
    private val repository = TasksRepository.getInstance(database)


    private val _eventSaveText = MutableLiveData<String>()
    val eventSaveText: LiveData<String>
        get() = _eventSaveText

    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val isCompleted = MutableLiveData<Boolean>()


    fun getTask(taskId: Long): Task {
        var task = Task()
        runBlocking {
            val job = async { repository.getTaskById(taskId) }
            task = job.await()
        }
        if (task == null) {
            title.value = ""
            description.value = ""
            isCompleted.value = false
        } else {

            title.value = task.title
            description.value = task.description
            isCompleted.value = task.isCompleted
        }

        return task
    }

    fun saveToDatabase() {
        uiScope.launch {
            if (title.value.isNullOrEmpty() && description.value.isNullOrEmpty()) {
                _eventSaveText.value = context.getString(R.string.task_not_saved)
            } else {
                if (title.value.isNullOrEmpty())
                    title.value = ""
                if (description.value.isNullOrEmpty())
                    description.value = ""
                if (taskId == -1L) {
                    val task = Task(title = title.value!!, description = description.value!!)
                    repository.insertTask(task)
                } else {
                    val task = Task(
                        id = taskId,
                        title = title.value!!,
                        description = description.value!!,
                        isCompleted = isCompleted.value!!
                    )
                    repository.update(task)
                }
                _eventSaveText.value = context.getString(R.string.task_saved)
            }
        }
    }

    fun doneSave() {
        _eventSaveText.value = null
    }

}