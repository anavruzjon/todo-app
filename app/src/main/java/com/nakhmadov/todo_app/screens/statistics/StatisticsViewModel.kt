package com.nakhmadov.todo_app.screens.statistics

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.nakhmadov.todo_app.data.TasksRepository
import com.nakhmadov.todo_app.data.db.getDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {

    private val job = Job()
    private val uiScope = CoroutineScope(job + Dispatchers.Main)

    val allCount = MutableLiveData<String>()

    val activeCount = MutableLiveData<String>()
    val activePercent = MutableLiveData<String>()

    val completedCount = MutableLiveData<String>()
    val completedPercent = MutableLiveData<String>()

    private val database = getDatabase(application)
    private val repository = TasksRepository.getInstance(database)

    init {
        uiScope.launch {
            val allCountLong = repository.getAllCount()
            allCount.value = allCountLong.toString()

            val activeCountLong = repository.getActiveCount()
            activeCount.value = activeCountLong.toString()
            activePercent.value = String.format("%.1f", (activeCountLong * 100.0) / allCountLong) + "%"

            val completedCountLong = repository.getCompletedCount()
            completedCount.value = completedCountLong.toString()
            completedPercent.value = String.format("%.1f", (completedCountLong * 100.0) / allCountLong) + "%"
            Log.d("myLogs", "all: $allCountLong, active: $activeCountLong, completed: $completedCountLong")
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
