package com.alamin.pillreminder.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
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