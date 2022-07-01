package com.alamin.pillreminder.model.data

data class RecentSchedule(
    val pillId: Int,
    var pillName: String,
    var pillType: String,
    val pillUnit: String,
    val time : String,
    val unit: Double,
)