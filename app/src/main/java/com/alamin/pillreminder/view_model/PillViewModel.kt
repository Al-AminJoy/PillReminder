package com.alamin.pillreminder.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alamin.pillreminder.model.data.Pill
import com.alamin.pillreminder.model.data.RecentSchedule
import com.alamin.pillreminder.model.repository.PillRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

private const val TAG = "PillViewModel"

private const val DAY_MILLI_SEC_UNIT = 86400000

class PillViewModel @Inject constructor(private val pillRepository: PillRepository) : ViewModel() {
     val isContinuous = MutableLiveData<Boolean>();

    fun setContinuous(continuous: Boolean){
        isContinuous.value = continuous
    }

    fun insertPill(pill: Pill){
        viewModelScope.launch(IO) {
            pillRepository.insertPill(pill)
        }
    }

    fun getAllPill(): LiveData<List<Pill>>{
        return pillRepository.getAllPill();
    }

    fun getTodayPill(pill: List<Pill>): List<Pill> {

        var todayPillList = arrayListOf<Pill>()

            for (pill in pill){
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
        return todayPillList

    }

    fun getRecentPillData(todayPillList: List<Pill>): List<RecentSchedule> {
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
        return recentPillList
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