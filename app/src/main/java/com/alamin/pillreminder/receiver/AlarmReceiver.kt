package com.alamin.pillreminder.receiver

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.alamin.pillreminder.R
import com.alamin.pillreminder.model.data.Pill
import com.alamin.pillreminder.service.AlarmService
import com.alamin.pillreminder.utils.Constants
import com.alamin.pillreminder.view.activity.MainActivity
import java.util.*
import java.util.concurrent.TimeUnit

private const val TAG = "AlarmReceiver"
class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val timeInMillis = intent.getLongExtra(Constants.SET_EXTRA_EXACT_ALARM,0L)
        val timeInterval = intent.getIntExtra(Constants.SET_INTERVAL,0)
        val pill = intent.getParcelableExtra<Pill>(Constants.SET_PILL)
        val isWeekly = intent.getBooleanExtra(Constants.SET_WEEKDAY,false)
        if (intent.action == Constants.SET_EXACT_ALARM){
            //sendNotification(context,"Exact Alarm",timeInMillis);
        }else{

            if (isWeekly){
                var currentDayofWeek = 0;
                val currentTime = System.currentTimeMillis()
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = currentTime
                with(calendar){
                    currentDayofWeek = get(Calendar.DAY_OF_WEEK)
                }

                pill?.let {
                    it.dayHolder?.let{
                        var nextInterval = 0;

                        if (pill.dayHolder.dayList != null){
                            var nextDay = 0;
                            for (day in pill?.dayHolder?.dayList){
                                val currentDateNumber = dateNumber(day)
                                if (currentDateNumber == currentDayofWeek){
                                    val currentIndex = pill.dayHolder.dayList.indexOf(day)
                                    if (currentIndex == (pill.dayHolder.dayList.size-1)){
                                        nextDay = dateNumber(pill.dayHolder.dayList[0])
                                        nextInterval = currentDateNumber - (currentDateNumber-nextDay)
                                    }else{
                                        nextDay = dateNumber(pill.dayHolder.dayList[currentIndex+0])
                                        nextInterval = nextDay - currentDateNumber
                                    }
                                    break
                                }
                            }
                        }
                        val  cal = Calendar.getInstance().apply {
                            this.timeInMillis = timeInMillis + TimeUnit.DAYS.toMillis(nextInterval.toLong())
                        }
                        AlarmService(context).setRepetitiveAlarm(cal.timeInMillis,timeInterval,pill)
                    }

                }


            }else{
                val  cal = Calendar.getInstance().apply {
                    this.timeInMillis = timeInMillis + TimeUnit.DAYS.toMillis(timeInterval.toLong())
                }
                AlarmService(context).setRepetitiveAlarm(cal.timeInMillis,timeInterval,pill)
            }


            sendNotification(context,"Pill Reminder",pill?.pillName,timeInMillis);
        }

    }

    private fun dateNumber(currentDayofWeek: String): Int {
        return when(currentDayofWeek){
            "Sunday" -> 1
            "Monday" -> 2
            "Tuesday" -> 3
            "Wednesday" -> 4
            "Thursday" -> 5
            "Friday" -> 6
            else -> 7
        }

    }

    private fun sendNotification(context: Context, title: String, message: String?, timeInMillis: Long) {
        val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(context,
            Constants.NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setOngoing(false)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = NotificationManagerCompat.from(context)
        val resultIntent = Intent(context, MainActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 0)
        mBuilder.setContentIntent(resultPendingIntent)
        notificationManager.notify(999,mBuilder.build())
    }
}