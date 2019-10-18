package com.nakhmadov.todo_app.screens.tasks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.nakhmadov.todo_app.R
import com.nakhmadov.todo_app.data.Task
import com.nakhmadov.todo_app.data.TasksRepository
import com.nakhmadov.todo_app.data.db.getDatabase
import com.nakhmadov.todo_app.util.ACTIVE_TASKS
import com.nakhmadov.todo_app.util.ALL_TASKS
import com.nakhmadov.todo_app.util.COMPLETED_TASKS
import kotlinx.coroutines.*

class TasksViewModel(application: Application) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application.applicationContext)
    private val repository = TasksRepository.getInstance(database = database)

    private val context = application.applicationContext

    private val _filterText = MutableLiveData<String>()
    val filterText: LiveData<String>
        get() = _filterText

    private val filteringMenuText = MutableLiveData<String>()
    private lateinit var completedList: List<Task>

    private val _eventStatusChange = MutableLiveData<String>()
    val eventStatusChange: LiveData<String>
        get() = _eventStatusChange


    private val _eventRefresh = MutableLiveData<Boolean>()
    val eventRefresh: LiveData<Boolean>
        get() = _eventRefresh

    val tasks: LiveData<List<Task>> =
        Transformations.switchMap(filteringMenuText) { taskType ->
            when (taskType) {
                ALL_TASKS -> {
                    _filterText.value = context.getString(R.string.all_tasks)
                    repository.getAll()
                }
                ACTIVE_TASKS -> {
                    _filterText.value = context.getString(R.string.active_tasks)
                    repository.getActive()
                }
                COMPLETED_TASKS -> {
                    _filterText.value = context.getString(R.string.completed_tasks)
                    repository.getCompleted()
                }
                else -> {
                    _filterText.value = context.getString(R.string.all_tasks)
                    repository.getAll()
                }
            }
        }

    init {
        _filterText.value = context.getString(R.string.all_tasks)
        filteringMenuText.value = ALL_TASKS
    }

    fun refreshTasks() {
        _eventRefresh.value = true
        // TODO REFRESH TASKS
    }

    fun doneRefresh() {
        _eventRefresh.value = null
    }

    fun deleteCompleted() {
        uiScope.launch {
            completedList = repository.getCompletedList()
            repository.deleteCompleted()
        }
    }

    fun undoDeleteComplete() {
        uiScope.launch {
            repository.insertListTask(completedList)
        }
    }

    fun getCompletedCount(): Long {
        var count = 0L
        runBlocking {
            val job = async { repository.getCompletedCount() }
            count = job.await()
        }
        return count
    }

    private fun getTaskById(id: Long): Task {
        var task = Task()
        runBlocking {
            val job = async { repository.getTaskById(id) }
            task = job.await()
        }
        return task
    }

    private fun updateTask(task: Task) {
        uiScope.launch {
            repository.update(task)
        }
    }

    fun filteringMenuChange(tasksType: String) {
        filteringMenuText.value = tasksType
    }

    fun changeStatus(id: Long) {
        val task = getTaskById(id)
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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
