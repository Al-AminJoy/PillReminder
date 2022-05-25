package com.alamin.pillreminder.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class Pill(@PrimaryKey(autoGenerate = true) private val id: Int, private val pillName: String) {
}