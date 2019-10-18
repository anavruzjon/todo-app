package com.nakhmadov.todo_app.screens.tasks

import com.nakhmadov.todo_app.data.Task

class TaskListener(val clickListener: (id: Long) -> Unit, val checkedChangeListener: (id: Long) -> Unit) {
    fun onTaskClick(task: Task) = clickListener(task.id)
    fun onCheckChange(task: Task) = checkedChangeListener(task.id)
}