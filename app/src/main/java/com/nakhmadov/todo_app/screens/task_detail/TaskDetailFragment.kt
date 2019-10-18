package com.nakhmadov.todo_app.screens.task_detail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController

import com.nakhmadov.todo_app.R
import com.nakhmadov.todo_app.databinding.TaskDetailFragmentBinding

class TaskDetailFragment : Fragment() {

    private lateinit var viewModel: TaskDetailViewModel
    private lateinit var binding: TaskDetailFragmentBinding
    private var taskId = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        taskId = TaskDetailFragmentArgs.fromBundle(arguments!!).taskId
        binding = TaskDetailFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        val application = requireNotNull(activity).application

        val viewModelFactory = TaskDetailViewModelFactory(taskId, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[TaskDetailViewModel::class.java]

        binding.viewModel = viewModel
        binding.task = viewModel.getTask(taskId)
        binding.lifecycleOwner = this

        viewModel.eventStatusChange.observe(this, Observer {
            it?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                viewModel.statusChanged()
            }
        })

        binding.editTask.setOnClickListener {
            navigateToEditTaskFragment(taskId)
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun navigateToEditTaskFragment(taskId: Long) {
        findNavController().navigate(
            TaskDetailFragmentDirections.actionTaskDetailFragmentToAddEditTaskFragment(
                taskId
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.task_detail_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {

            Toast.makeText(context, context?.getString(R.string.task_deleted), Toast.LENGTH_LONG).show()
            viewModel.deleteTask(taskId)
            findNavController().navigate(TaskDetailFragmentDirections.actionTaskDetailFragmentToTasksFragment())
            return true
        }
        return false
    }

    companion object {
        fun newInstance() = TaskDetailFragment()
    }

}
