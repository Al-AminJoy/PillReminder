package com.alamin.pillreminder.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RecentSchedule(
    val pillId: Int,
    var pillName: String,
    var pillType: String,
    val pillUnit: String,
    val mealStatus: String,
    val time : String,
    val unit: Double,
):Parcelable