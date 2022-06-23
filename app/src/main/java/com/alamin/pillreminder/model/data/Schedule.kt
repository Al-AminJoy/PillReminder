package com.alamin.pillreminder.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*
@Parcelize
data class Schedule (
    val time : String,
    val unit: Double,
    ):Parcelable