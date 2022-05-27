package com.alamin.pillreminder.model.repository

import com.alamin.pillreminder.model.local.database.LocalDatabase
import javax.inject.Inject

class PillRepository @Inject constructor(private val localDatabase: LocalDatabase) {
}