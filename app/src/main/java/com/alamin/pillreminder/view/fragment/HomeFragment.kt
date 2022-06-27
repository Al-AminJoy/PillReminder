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
import java.sql.Date
import java.util.*
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

        var todayPillList = arrayListOf<RecentSchedule>()
        pillViewModel.getAllPill().observe(requireActivity(), Observer {
            todayPillList.clear()

            for (pill in it){
                for (schedule in pill.scheduleHolder.scheduleList){
                    var pillTime = schedule.time.substring(0,5).split(":")

                    var hourInMilliSec = 0
                    var minuteInMilliSec = 0

                    if (schedule.time.contains("PM")){
                        hourInMilliSec = (pillTime[0].trim().toInt()+12) * 3600000
                        minuteInMilliSec = pillTime[1].trim().toInt() * 60000
                    }else{
                        hourInMilliSec = (pillTime[0].trim().toInt()) * 3600000
                        minuteInMilliSec = (pillTime[1].trim().toInt()) * 60000
                    }

                    val pillTakingTime = hourInMilliSec+minuteInMilliSec

                    val calender = Calendar.getInstance()
                    val hour = calender.get(Calendar.HOUR_OF_DAY)
                    val minute = calender.get(Calendar.MINUTE)
                    val currentTimeInMilliSec = hour*3600000 + minute*60000
                    Log.d(TAG, "onCreateView: ${pillTakingTime}")

                    if (pillTakingTime-currentTimeInMilliSec in 1..1800000){
                        todayPillList.add(RecentSchedule(pill.id,pill.pillName,pill.pillUnit,schedule.time,schedule.unit))
                    }
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