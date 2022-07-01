package com.alamin.pillreminder.model.data

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Entity
@Parcelize
data class Pill(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var pillName: String,
    var pillType: String,
    val pillUnit: String,
    val pillStartTime: Long,
    val isContinuous: Boolean,
    val days: Int,
    val isEveryDay: Boolean,
    val dayHolder: @RawValue DayHolder,
    val dayInterval: Int,
    val scheduleHolder: @RawValue ScheduleHolder,
) : Parcelable