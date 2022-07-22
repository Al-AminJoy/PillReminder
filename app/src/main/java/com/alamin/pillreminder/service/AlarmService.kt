package com.alamin.pillreminder.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.alamin.pillreminder.model.data.Pill
import com.alamin.pillreminder.model.data.RecentSchedule
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.util.*
private const val DAY_MILLI_SEC_UNIT = 86400000

private const val TAG = "AlarmService"
class AlarmService : Service() {

    init {
        Log.d(TAG, "Service Is Running")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val data =  intent?.getParcelableArrayListExtra<Pill>("EXTRA")
        data?.let {
            CoroutineScope(IO).launch{
                while (true){
                    getRecentPillData(getTodayPill(data))
                }
            }
        }
        return START_STICKY
    }


    
    fun getTodayPill(pills: List<Pill>): List<Pill> {

        var todayPillList = arrayListOf<Pill>()

        for (pill in pills){
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

    suspend fun getRecentPillData(todayPillList: List<Pill>) {
        var recentPill : RecentSchedule? = null
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
                val second = calender.get(Calendar.SECOND)
                val currentTimeInMilliSec = hour*3600000 + minute*60000 + second
                if (pillTakingTime-currentTimeInMilliSec == 1800000){
                    Log.d(TAG, "If ${pillTakingTime-currentTimeInMilliSec}")

                       recentPill = RecentSchedule(pill.id,pill.pillName,pill.pillType,pill.pillUnit,schedule.mealStatus,schedule.time,schedule.unit)
                       withContext(Main){
                           Toast.makeText(applicationContext, "Pill Within 30 min", Toast.LENGTH_SHORT).show()
                           delay(1000)
                       }
                }else if (pillTakingTime-currentTimeInMilliSec == 0){
                    Log.d(TAG, "Else If ${pillTakingTime-currentTimeInMilliSec}")
                    withContext(Main){
                        Toast.makeText(applicationContext, "Pill Time", Toast.LENGTH_SHORT).show()
                        delay(1000)
                    }
                }else{
                    Log.d(TAG, "Else ${pillTakingTime-currentTimeInMilliSec}")
                }
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
    
    override fun onBind(intent: Intent): IBinder? = null

}