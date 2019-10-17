package com.nakhmadov.todo_app.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nakhmadov.todo_app.data.Task

@Dao
interface TasksDao {

    @Query("select * from tasks_table order by id desc")
    fun getAll(): LiveData<List<Task>>

    @Query("select * from tasks_table where completed = 0 order by id desc")
    fun getActive(): LiveData<List<Task>>

    @Query("select * from tasks_table where completed = 1 order by id desc")
    fun getCompleted(): LiveData<List<Task>>

    @Query("select * from tasks_table where completed = 1")
    fun getCompletedList(): List<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task)

    @Query("delete from tasks_table where completed = 1")
    fun deleteCompleted()

    @Query("delete from tasks_table where id = :taskId")
    fun deleteTask(taskId: Long)

    @Query("select * from tasks_table where id = :taskId")
    fun getTaskById(taskId: Long): Task

    @Query("delete from tasks_table")
    fun clear()

    @Insert
    fun insertListTask(list: List<Task>)

    @Query("select count(id) from tasks_table where completed = 1")
    fun getCompletedCount(): Long

    @Update
    fun update(task: Task)
}