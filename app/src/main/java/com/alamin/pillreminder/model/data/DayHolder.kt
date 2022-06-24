package com.alamin.pillreminder.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DayHolder(
    val dayList: List<String>,
):Parcelable
