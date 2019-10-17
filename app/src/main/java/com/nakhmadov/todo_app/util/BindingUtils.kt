package com.nakhmadov.todo_app.util

import android.widget.CheckBox
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.nakhmadov.todo_app.data.Task

@BindingAdapter("setTitleTask")
fun TextView.setTitleTask(item: Task?) {
    item?.let {
        text = if (it.title.isNotEmpty()) it.title else it.description
    }
}

@BindingAdapter("isCompleted")
fun CheckBox.isCompleted(item: Task?) {
    item?.let {
        isChecked = it.isCompleted

    }
}