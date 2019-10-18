package com.nakhmadov.todo_app.data

import androidx.lifecycle.LiveData
import com.nakhmadov.todo_app.data.db.TasksDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TasksRepository(private val database: TasksDatabase) {

    var tasks = database.tasksDao.getAll()


    fun refreshTasks() {
        // TODO REFRESH TASKS
        // GET TASKS FROM THE INTERNET AND SAVE THEM TO THE DATABASE
    }


    fun getActive(): LiveData<List<Task>> {
        return database.tasksDao.getActive()
    }

    fun getAll(): LiveData<List<Task>> {
        return database.tasksDao.getAll()
    }

    fun getCompleted(): LiveData<List<Task>> {
        return database.tasksDao.getCompleted()
    }

    suspend fun getCompletedList(): List<Task> = withContext(Dispatchers.IO) {
        database.tasksDao.getCompletedList()
    }

    suspend fun insertTask(task: Task) = withContext(Dispatchers.IO) {
        database.tasksDao.insert(task = task)
    }


    suspend fun deleteCompleted() = withContext(Dispatchers.IO) {
        database.tasksDao.deleteCompleted()
    }


    suspend fun deleteTask(id: Long) = withContext(Dispatchers.IO) {
        database.tasksDao.deleteTask(taskId = id)
    }


    suspend fun clear() = withContext(Dispatchers.IO) {
        database.tasksDao.clear()
    }


    suspend fun insertListTask(list: List<Task>) = withContext(Dispatchers.IO) {
        database.tasksDao.insertListTask(list)
    }

    suspend fun getAllCount(): Long = withContext(Dispatchers.IO) {
        database.tasksDao.getAllCount()
    }

    suspend fun getActiveCount(): Long = withContext(Dispatchers.IO) {
        database.tasksDao.getActiveCount()
    }

    suspend fun getCompletedCount(): Long = withContext(Dispatchers.IO) {
        database.tasksDao.getCompletedCount()
    }

    suspend fun getTaskById(id: Long): Task = withContext(Dispatchers.IO) {
        database.tasksDao.getTaskById(taskId = id)
    }

    suspend fun update(task: Task) = withContext(Dispatchers.IO) {
        database.tasksDao.update(task)
    }

    companion object {
        private lateinit var INSTANCE: TasksRepository

        fun getInstance(database: TasksDatabase): TasksRepository {
            synchronized(TasksRepository::class.java) {
                if (!Companion::INSTANCE.isInitialized) {
                    INSTANCE =
                        TasksRepository(database = database)
                }
                return INSTANCE
            }
        }
    }

}