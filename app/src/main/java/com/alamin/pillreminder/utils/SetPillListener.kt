package com.alamin.pillreminder.utils

import com.alamin.pillreminder.model.data.Pill

interface SetPillListener {
   fun onFindPill(pillList: List<Pill>)
}