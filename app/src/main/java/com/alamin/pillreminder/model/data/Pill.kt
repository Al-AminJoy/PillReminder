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
    val pillName: String,
    val pillUnit: String,
    val pillStartTime: String,
    val isContinuous: Boolean,
    val days: Int,
    val scheduleHolder: @RawValue ScheduleHolder,
    val pillStock: Double,
    val minimumPillStock: Int
) : Parcelable