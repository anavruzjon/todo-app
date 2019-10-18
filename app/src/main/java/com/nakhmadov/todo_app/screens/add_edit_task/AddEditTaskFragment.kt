package com.nakhmadov.todo_app.screens.add_edit_task

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.nakhmadov.todo_app.R
import com.nakhmadov.todo_app.databinding.AddEditTaskFragmentBinding

class AddEditTaskFragment : Fragment() {



    private lateinit var binding: AddEditTaskFragmentBinding
    private lateinit var viewModel: AddEditTaskViewModel
    private var taskId = -1L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        taskId = AddEditTaskFragmentArgs.fromBundle(arguments!!).taskId
        binding = AddEditTaskFragmentBinding.inflate(inflater, container, false)
        val application = requireNotNull(activity?.application)
        val viewModelFactory = AddEditTaskViewModelFactory(taskId, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[AddEditTaskViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.eventSaveText.observe(this, Observer {
            it?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                viewModel.doneSave()
            }

        })

        val task = viewModel.getTask(taskId)
        if (taskId > 0) {
            binding.descriptionEditText.setText(task.description)
            binding.titleEditText.setText(task.title)
        }
        binding.doneButton.setOnClickListener {
            viewModel.saveToDatabase()
            hideInputManage()
            navigateToTasksFragment()
        }
        return binding.root

    }

    private fun hideInputManage() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun navigateToTasksFragment() {
        findNavController().navigate(AddEditTaskFragmentDirections.actionAddEditTaskFragmentToTasksFragment())
    }

    companion object {
        fun newInstance() = AddEditTaskFragment()
    }
}
