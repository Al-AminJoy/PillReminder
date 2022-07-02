package com.alamin.pillreminder.view.adapter

import com.alamin.pillreminder.model.data.Pill

interface OnPillListClickListener {
    fun onClick(pill: Pill)
}