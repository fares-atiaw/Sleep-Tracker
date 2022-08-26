package com.example.android.trackmysleepquality.sleeptracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.FragmentSleepTrackerBinding
import com.google.android.material.snackbar.Snackbar

/**
 * A fragment with buttons to record start and end times for sleep, which are saved in
 * a database. Cumulative data is displayed in a simple scrollable TextView.
 * (Because we have not learned about RecyclerView yet.)
 */
class SleepTrackerFragment : androidx.fragment.app.Fragment() {
lateinit var binding :FragmentSleepTrackerBinding
lateinit var adapter :SleepNightAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sleep_tracker, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = VMF_SleepTracker(dataSource, application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(VM_SleepTracker::class.java)

        binding.x = viewModel
        binding.lifecycleOwner = this
        adapter = SleepNightAdapter(SleepNightListener {
            viewModel.sleepNightClicked(it)
        })
        binding.rv.adapter = adapter
        binding.rv.layoutManager = GridLayoutManager(activity, 3)

        viewModel.allNights.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.go_to_quality.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate((SleepTrackerFragmentDirections.actionSleepTrackerFragmentToSleepQualityFragment(it.nightId)))
            }
        })
        viewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
        if (it == true) { // Observed state is true.
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                getString(R.string.cleared_message),
                Snackbar.LENGTH_SHORT // How long to display the message.
            ).show()
            viewModel.doneShowingSnackbar()
        }
        })

        viewModel.go_to_details.observe(viewLifecycleOwner, Observer {
            Log.w("hhhh", "went")
            it?.let {
                this.findNavController().navigate(
                    SleepTrackerFragmentDirections
                        .actionSleepTrackerFragmentToSleepDetailFragment(it)
                )
                viewModel.went()
            }
        })

        return binding.root
    }

}
