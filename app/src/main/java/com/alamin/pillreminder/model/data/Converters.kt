package com.alamin.pillreminder.model.data

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun fromScheduleHolder( scheduleHolder: ScheduleHolder): String{
        return Gson().toJson(scheduleHolder)
    }

    @TypeConverter
    fun toScheduleHolder(scheduleHolder: String): ScheduleHolder{
        return Gson().fromJson(scheduleHolder, ScheduleHolder::class.java);
    }
}