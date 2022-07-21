package com.alamin.pillreminder.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ScheduleHolder(
 var scheduleList: List<Schedule>
):Parcelable
