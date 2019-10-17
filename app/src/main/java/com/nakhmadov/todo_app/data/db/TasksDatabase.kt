package com.nakhmadov.todo_app.data.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nakhmadov.todo_app.data.Task

@Database(entities = [Task::class], version = 1)
abstract class TasksDatabase : RoomDatabase() {
    abstract val tasksDao: TasksDao
}

private lateinit var INSTANCE: TasksDatabase

fun getDatabase(application: Application): TasksDatabase {
    synchronized(TasksDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                application.applicationContext,
                TasksDatabase::class.java,
                "tasksDB"
            ).build()
        }
        return INSTANCE
    }
}
