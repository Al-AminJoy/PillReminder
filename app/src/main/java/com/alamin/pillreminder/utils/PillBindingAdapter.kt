package com.alamin.pillreminder.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.alamin.pillreminder.R
import com.alamin.pillreminder.model.data.Schedule

@BindingAdapter("showDoseInDetails","unit", requireAll = true)
fun TextView.showDoseInDetails(schedules: List<Schedule>,unit: String){
    var strBuilder =StringBuilder()
    strBuilder.append("${schedules.size} Times | At ")
    schedules.forEach {
        if (schedules.indexOf(it) == 0){
            strBuilder.append("${it.time} (${it.unit} $unit)")
        }else if (schedules.indexOf(it) == (schedules.size-1)){
            strBuilder.append(" & ${it.time} (${it.unit} $unit) ")
        }else {
            strBuilder.append(", ${it.time} (${it.unit} $unit)")
        }
    }
    this.text = strBuilder
}

@BindingAdapter("showDose")
fun TextView.showDose(schedules: List<Schedule>){
    var strBuilder =StringBuilder()
    strBuilder.append("At ")
    schedules.forEach {
        if (schedules.indexOf(it) == 0){
            strBuilder.append("${it.time} ")
        }else {
            strBuilder.append("| ${it.time}")
        }
    }
    this.text = strBuilder
}

@BindingAdapter("showTimes")
fun TextView.showTimes(schedules: List<Schedule>){
    var strBuilder =StringBuilder()
    strBuilder.append("${schedules.size} Times")
    this.text = strBuilder
}

@BindingAdapter("showTotalUnits","unit", requireAll = true)
fun TextView.showTotalUnits(schedules: List<Schedule>, unit: String){
    var strBuilder =StringBuilder()
    strBuilder.append("Total ${schedules.sumOf {it.unit }} $unit")
    this.text = strBuilder
}

@BindingAdapter("showSchedule","isEveryDay","dayInterval")
fun TextView.showSchedule(dayList:List<String>,isEveryDay:Boolean,dayInterval : Int){
    var strBuilder =StringBuilder()
    if (dayList.isNotEmpty()){
        dayList.forEach {
            if (dayList.indexOf(it) == 0){
                strBuilder.append("$it")
            }else if (dayList.indexOf(it) == (dayList.size-1)){
                strBuilder.append(" & $it")
            }else {
                strBuilder.append(", $it")
            }
        }
    }
    if (isEveryDay){
        this.text = "Everyday"
    }else {
        "Every ${if (dayInterval != 0) "$dayInterval Day" else strBuilder}".also { this.text = it }
    }
}

@BindingAdapter("showPillIcon")
fun ImageView.showPillIcon(unit: String){
    when(unit){
        "Tablet" -> this.setImageDrawable(resources.getDrawable(R.drawable.tablet,null))
        "Capsule" -> this.setImageDrawable(resources.getDrawable(R.drawable.capsule,null))
        "Syrup" -> this.setImageDrawable(resources.getDrawable(R.drawable.syrup,null))
        "Injection" -> this.setImageDrawable(resources.getDrawable(R.drawable.injection,null))
        else -> this.setImageDrawable(resources.getDrawable(R.drawable.eye_drops,null))
    }
}