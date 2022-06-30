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
import androidx.recyclerview.widget.LinearLayoutManager
import com.alamin.pillreminder.PillApplication
import com.alamin.pillreminder.R
import com.alamin.pillreminder.databinding.FragmentHomeBinding
import com.alamin.pillreminder.model.data.Pill
import com.alamin.pillreminder.view.adapter.RecentPillAdapter
import com.alamin.pillreminder.view.adapter.TodayPillAdapter
import com.alamin.pillreminder.view_model.PillViewModel
import com.alamin.pillreminder.view_model.ViewModelFactory
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

private const val TAG = "HomeFragment"
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
//For Recent Pill
/*        for (pill in it){
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
                    recentPillList.add(RecentSchedule(pill.id,pill.pillName,pill.pillUnit,schedule.time,schedule.unit))
                }
            }
        }*/

        var todayPillList = arrayListOf<Pill>()
        pillViewModel.getAllPill().observe(requireActivity(), Observer {
            todayPillList.clear()

            for (pill in it){
                var currentDay = 0;
                var currentDayofWeek = 0;
                var currentMonth = 0;
                var currentYear = 0;
                var startDay = 0;
                var startMonth = 0;
                var startYear = 0;
                val currentTime = System.currentTimeMillis()
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = currentTime
                with(calendar){
                    currentDay = get(Calendar.DAY_OF_MONTH)
                    currentDayofWeek = get(Calendar.DAY_OF_WEEK)
                    currentMonth = get(Calendar.MONTH)
                    currentYear = get(Calendar.YEAR)
                }

                calendar.timeInMillis = pill.pillStartTime

                with(calendar){
                    startDay = get(Calendar.DAY_OF_MONTH)
                    startMonth = get(Calendar.MONTH)
                    startYear = get(Calendar.YEAR)
                }

               if (currentTime >= pill.pillStartTime){
                   if (pill.isContinuous){
                       filterPill(todayPillList,pill,currentDayofWeek,startDay,currentDay)
                   }else {
                       filterPill(todayPillList,pill,currentDayofWeek,startDay,currentDay)
                   }
               }

            }
           // Log.d(TAG, "onCreateView: ${todayPillList}")
            todayPillAdapter.setData(todayPillList)
        })


        binding.setFloatingClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_pillNameDialogFragment)
        }
        return binding.root
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