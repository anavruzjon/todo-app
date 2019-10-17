package com.nakhmadov.todo_app.screens.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nakhmadov.todo_app.data.Task
import com.nakhmadov.todo_app.databinding.TaskItemBinding

class TasksAdapter (private val taskListener: TaskListener) : ListAdapter<Task, TasksAdapter.TaskHolder>(TaskDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        return TaskHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, taskListener)
    }

    class TaskHolder(private val binding: TaskItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task, listener: TaskListener) {
            binding.task = task
            binding.listener = listener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup) : TaskHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TaskItemBinding.inflate(layoutInflater, parent, false)
                return TaskHolder(binding)
            }
        }
    }

}