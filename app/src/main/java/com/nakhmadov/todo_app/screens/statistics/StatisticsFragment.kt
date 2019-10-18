package com.nakhmadov.todo_app.screens.statistics

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

import com.nakhmadov.todo_app.R
import com.nakhmadov.todo_app.databinding.AddEditTaskFragmentBinding
import com.nakhmadov.todo_app.databinding.StatisticsFragmentBinding

class StatisticsFragment : Fragment() {

    private lateinit var viewModel: StatisticsViewModel
    private lateinit var binding: StatisticsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = StatisticsFragmentBinding.inflate(inflater, container, false)

        val application = requireNotNull((activity as AppCompatActivity).application)
        val viewModelFactory = StatisticsViewModelFactory(application)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[StatisticsViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.statistics_fragment_title)

        return binding.root
    }

}
