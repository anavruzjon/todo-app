package com.nakhmadov.todo_app.screens.tasks

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar

import com.nakhmadov.todo_app.R
import com.nakhmadov.todo_app.databinding.TasksFragmentBinding
import com.nakhmadov.todo_app.util.ACTIVE_TASKS
import com.nakhmadov.todo_app.util.ALL_TASKS
import com.nakhmadov.todo_app.util.COMPLETED_TASKS

class TasksFragment : Fragment() {

    private lateinit var binding: TasksFragmentBinding
    private lateinit var viewModel: TasksViewModel
    private lateinit var tasksAdapter: TasksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.tasks_fragment, container, false)
        binding.lifecycleOwner = this

        val application = requireNotNull(activity).application
        val viewModelFactory = TasksViewModelFactory(application)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[TasksViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupRecycler()

        viewModel.eventStatusChange.observe(this, Observer {
            it?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                viewModel.statusChanged()
            }
        })

        viewModel.eventRefresh.observe(this, Observer {
            it?.let {
                if (it) {
                    viewModel.doneRefresh()
                }
            }
        })

        binding.newTaskButton.setOnClickListener {
            addNewTask()
        }
        binding.fabAddTask.setOnClickListener {
            addNewTask()
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun setupRecycler() {
        binding.tasksRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.tasksRecyclerView.setHasFixedSize(true)

        tasksAdapter = TasksAdapter(
            TaskListener(clickListener = { taskId ->
                navigateToTaskDetailFragment(taskId)
            },
                checkedChangeListener = { taskId ->
                    viewModel.changeStatus(taskId)
                })
        )

        viewModel.tasks.observe(viewLifecycleOwner, Observer {
            tasksAdapter.submitList(it)
        })

        binding.tasksRecyclerView.adapter = tasksAdapter
    }

    private fun navigateToTaskDetailFragment(taskId: Long) {
        findNavController().navigate(TasksFragmentDirections.actionTasksFragmentToTaskDetailFragment(taskId))
    }

    private fun addNewTask() {
        findNavController().navigate(TasksFragmentDirections.actionTasksFragmentToAddEditTaskFragment())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tasks_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val clearCount = viewModel.getCompletedCount()
        when (item.itemId) {
            R.id.menu_refresh -> {
                viewModel.refreshTasks()
                return true
            }
            R.id.menu_clear_completed -> {
                viewModel.deleteCompleted()
                val snackbar = Snackbar.make(
                    binding.coordinatorLayout,
                    "$clearCount ${context?.getString(R.string.tasks_deleted)}",
                    Snackbar.LENGTH_LONG
                )
                val actionTextView =
                    snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
                actionTextView.isAllCaps = false
                snackbar.setAction(R.string.on_delete_completed_cancel) {
                    viewModel.undoDeleteComplete()
                }
                snackbar.show()
                return true
            }

            R.id.menu_filter -> {
                filteringPopUpMenu()
                return true
            }

        }
        return false
    }

    private fun filteringPopUpMenu() {

        val view = activity?.findViewById<View>(R.id.menu_filter) ?: return
        val popupMenu = PopupMenu(context!!, view)

        popupMenu.run {
            inflate(R.menu.filter_tasks)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.all -> {
                        viewModel.filteringMenuChange(ALL_TASKS)
                        return@setOnMenuItemClickListener true
                    }
                    R.id.active -> {
                        viewModel.filteringMenuChange(ACTIVE_TASKS)
                        return@setOnMenuItemClickListener true

                    }
                    R.id.completed -> {
                        viewModel.filteringMenuChange(COMPLETED_TASKS)
                        return@setOnMenuItemClickListener true

                    }
                }
                return@setOnMenuItemClickListener false
            }
            show()
        }
    }




    companion object {
        fun newInstance() = TasksFragment()
    }

}
