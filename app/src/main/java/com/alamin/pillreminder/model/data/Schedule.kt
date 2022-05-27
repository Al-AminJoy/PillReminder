package com.alamin.pillreminder.model.data

import java.util.*

data class Schedule (
    val time : String,
    val unit: Double,
    val isEveryDay: Boolean,
    val dayList: List<String>,
    val dayInterval: Int
    )