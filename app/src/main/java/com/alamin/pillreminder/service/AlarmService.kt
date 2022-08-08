package com.alamin.pillreminder.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.alamin.pillreminder.model.data.Pill
import com.alamin.pillreminder.receiver.AlarmReceiver
import com.alamin.pillreminder.utils.Constants
import com.alamin.pillreminder.utils.RandomIntUtil
import com.google.gson.Gson

private const val TAG = "AlarmService"
class AlarmService (private val context: Context)   {

    private val alarmManager: AlarmManager ? = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setExactAlarm(timeInMillis: Long){
        setAlarm(timeInMillis,
                getPendingIntent(
                    getIntent().apply {
                        action = Constants.SET_EXACT_ALARM
                        putExtra(Constants.SET_EXTRA_EXACT_ALARM,timeInMillis)
                    }
                ))
    }

    fun setRepetitiveAlarm(timeInMillis: Long,interval: Int?, pill: Pill?){
        val pillAsByteArray = Gson().toJson(pill)
        setAlarm(timeInMillis,
            getPendingIntent(
                getIntent().apply {
                    action = Constants.SET_REPETITIVE_ALARM
                    putExtra(Constants.SET_EXTRA_EXACT_ALARM,timeInMillis)
                    if (interval == null){
                        putExtra(Constants.SET_WEEKDAY,true)
                    }
                    putExtra(Constants.SET_INTERVAL,interval)
                    putExtra(Constants.SET_PILL,pillAsByteArray)
                }
            ))
    }

    fun setAlarm(timeInMillis: Long, pendingIntent: PendingIntent){
        alarmManager?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )
            }else{
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )
            }
        }
    }

    private fun getIntent(): Intent = Intent(context, AlarmReceiver::class.java)

    private fun getPendingIntent(intent: Intent) : PendingIntent = PendingIntent.getBroadcast(
        context,
        RandomIntUtil.getRandomInt(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

}