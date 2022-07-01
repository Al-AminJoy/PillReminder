package com.alamin.pillreminder.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.alamin.pillreminder.R
import com.alamin.pillreminder.model.data.Schedule

@BindingAdapter("showSchedules","unit", requireAll = true)
fun TextView.showSchedules(schedules: List<Schedule>,unit: String) {
    var pillTime = StringBuilder()
    for (schedule in schedules){
        if (schedules.indexOf(schedule) == 0){
            pillTime.append("${schedule.unit} $unit ").append("At ").append(schedule.time)
        }else{
            pillTime.append("\n${schedule.unit} $unit ").append("At ").append(schedule.time)
        }
    }
    this.text = pillTime
}

@BindingAdapter("showPillIcon")
fun ImageView.showPillIcon(unit: String){
    when(unit){
        "Tablet" -> this.setImageDrawable(resources.getDrawable(R.drawable.tablet,null))
        "Capsule" -> this.setImageDrawable(resources.getDrawable(R.drawable.capsul,null))
        "Syrup" -> this.setImageDrawable(resources.getDrawable(R.drawable.syrup,null))
        "Injection" -> this.setImageDrawable(resources.getDrawable(R.drawable.injection,null))
        else -> this.setImageDrawable(resources.getDrawable(R.drawable.eye_drops,null))
    }
}