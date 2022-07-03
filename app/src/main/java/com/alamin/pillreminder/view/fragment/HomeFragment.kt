package com.alamin.pillreminder.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.alamin.pillreminder.PillApplication
import com.alamin.pillreminder.R
import com.alamin.pillreminder.databinding.FragmentHomeBinding
import com.alamin.pillreminder.model.data.Pill
import com.alamin.pillreminder.model.data.RecentSchedule
import com.alamin.pillreminder.view.adapter.RecentPillAdapter
import com.alamin.pillreminder.view.adapter.TodayPillAdapter
import com.alamin.pillreminder.view_model.PillViewModel
import com.alamin.pillreminder.view_model.ViewModelFactory
import java.util.*
import javax.inject.Inject

private const val TAG = "HomeFragment"
private const val DAY_MILLI_SEC_UNIT = 86400000

class HomeFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var recentPillAdapter: RecentPillAdapter
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
        binding.recyclerViewRecentPill.apply {
            layoutManager = GridLayoutManager(requireContext(),2)
            adapter = recentPillAdapter
        }

        pillViewModel.getAllPill().observe(requireActivity(), Observer {
            var todayPillList = arrayListOf<Pill>()
            for (pill in it){
                var currentDay = 0;
                var currentDayofWeek = 0;
                var startDay = 0;
                val currentTime = System.currentTimeMillis()
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = currentTime
                with(calendar){
                    currentDay = get(Calendar.DAY_OF_MONTH)
                    currentDayofWeek = get(Calendar.DAY_OF_WEEK)
                }

                calendar.timeInMillis = pill.pillStartTime

                with(calendar){
                    startDay = get(Calendar.DAY_OF_MONTH)
                }

               if (currentTime >= pill.pillStartTime){
                   if (pill.isContinuous){
                       filterPill(todayPillList,pill,currentDayofWeek,startDay,currentDay)
                   }else {
                       val dayInMilliSec = pill.days * DAY_MILLI_SEC_UNIT
                       val pillEndDate = dayInMilliSec+pill.pillStartTime
                       if (currentTime <= pillEndDate){
                           filterPill(todayPillList,pill,currentDayofWeek,startDay,currentDay)
                       }
                   }
               }

            }
            if (todayPillList.size <= 0){
                binding.txtToday.text = "Woo !! No Pill in Today"
                binding.cardRecentPill.visibility = View.GONE
            }else{
                todayPillAdapter.setData(todayPillList)
                setRecentPillData(todayPillList)
            }
        })


        binding.setFloatingClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_pillNameDialogFragment)
        }
        return binding.root
    }

    private fun setRecentPillData(todayPillList: ArrayList<Pill>) {
        var recentPillList = arrayListOf<RecentSchedule>()
        for (pill in todayPillList){
            for (schedule in pill.scheduleHolder.scheduleList){
                var pillTime = schedule.time.substring(0,5).split(":")

                var hourInMilliSec = 0
                var minuteInMilliSec = 0

                if (schedule.time.contains("PM")){
                    val pillHour = pillTime[0].trim().toInt()
                    val pillMinute = pillTime[1].trim().toInt()
                    if (pillHour == 12){
                        hourInMilliSec = pillHour * 3600000
                        minuteInMilliSec = pillMinute * 60000
                    }else{
                        hourInMilliSec = (pillHour+12) * 3600000
                        minuteInMilliSec = pillMinute * 60000
                    }

                }else{
                    val pillHour = pillTime[0].trim().toInt()
                    val pillMinute = pillTime[1].trim().toInt()
                    if (pillHour == 12){
                        hourInMilliSec = 0 * 3600000
                        minuteInMilliSec = pillMinute * 60000
                    }else{
                        hourInMilliSec =   pillHour * 3600000
                        minuteInMilliSec = pillMinute * 60000
                    }

                }

                val pillTakingTime = hourInMilliSec+minuteInMilliSec

                val calender = Calendar.getInstance()
                val hour = calender.get(Calendar.HOUR_OF_DAY)
                val minute = calender.get(Calendar.MINUTE)
                val currentTimeInMilliSec = hour*3600000 + minute*60000
                if (pillTakingTime-currentTimeInMilliSec in 1..1800000){
                    recentPillList.add(RecentSchedule(pill.id,pill.pillName,pill.pillType,pill.pillUnit,schedule.mealStatus,schedule.time,schedule.unit))
                }
            }
        }

        if (recentPillList.size <= 0){
            binding.txtRecentPill.text = "Woo !! No Upcoming Pill"
        }else{
            recentPillAdapter.setData(recentPillList);
        }
    }

    private fun filterPill(
        todayPillList: ArrayList<Pill>,
        pill: Pill,
        currentDayofWeek: Int,
        startDay: Int,
        currentDay: Int
    ) {
        if (pill.isEveryDay){
            todayPillList.add(pill)
        }else if (pill.dayHolder.dayList.isNotEmpty()){
            val currentDayName: String = dateName(currentDayofWeek);
            for (day in pill.dayHolder.dayList){
                if (day == currentDayName){
                    todayPillList.add(pill)
                    break
                }
            }

        }else{
            if (currentDay-startDay == 0){
                todayPillList.add(pill)
            }else if ((currentDay-startDay) % pill.dayInterval == 0){
                todayPillList.add(pill)
            }
        }
    }

    private fun dateName(currentDayofWeek: Int): String {
       return when(currentDayofWeek){
            1 -> "Sunday"
            2 -> "Monday"
            3 -> "Tuesday"
            4 -> "Wednesday"
            5 -> "Thursday"
            6 -> "Friday"
           else -> "Saturday"
        }

    }

}