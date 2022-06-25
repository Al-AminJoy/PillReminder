package com.alamin.pillreminder.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alamin.pillreminder.PillApplication
import com.alamin.pillreminder.R
import com.alamin.pillreminder.databinding.FragmentHomeBinding
import com.alamin.pillreminder.model.data.RecentSchedule
import com.alamin.pillreminder.view.adapter.TodayPillAdapter
import com.alamin.pillreminder.view_model.PillViewModel
import com.alamin.pillreminder.view_model.ViewModelFactory
import javax.inject.Inject

private const val TAG = "HomeFragment"
class HomeFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var todayPillAdapter: TodayPillAdapter
    private lateinit var pillViewModel: PillViewModel
    private lateinit var binding: FragmentHomeBinding;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        val component = (requireActivity().applicationContext as PillApplication).appComponent
        component.injectHome(this)
        pillViewModel = ViewModelProvider(this,viewModelFactory)[PillViewModel::class.java]


        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = todayPillAdapter
        }
        pillViewModel.getAllPill().observe(requireActivity(), Observer {
            var todayPillList = arrayListOf<RecentSchedule>()

            for (pill in it){
                for (schedule in pill.scheduleHolder.scheduleList){
                    todayPillList.add(RecentSchedule(pill.id,pill.pillName,pill.pillUnit,schedule.time,schedule.unit))
                }
            }
            Log.d(TAG, "onCreateView: ${todayPillList}")
            todayPillAdapter.setData(todayPillList)
        })





        binding.setFloatingClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_pillNameDialogFragment)
        }
        return binding.root
    }

}