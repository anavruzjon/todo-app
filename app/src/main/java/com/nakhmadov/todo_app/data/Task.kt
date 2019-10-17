package com.nakhmadov.todo_app.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks_table")
data class Task(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "description")
    var description: String = "",

    @ColumnInfo(name = "completed")
    var isCompeted: Boolean = false
)