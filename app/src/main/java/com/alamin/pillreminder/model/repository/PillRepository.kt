package com.alamin.pillreminder.model.repository

import androidx.lifecycle.LiveData
import com.alamin.pillreminder.model.data.Pill
import com.alamin.pillreminder.model.local.database.LocalDatabase
import javax.inject.Inject

class PillRepository @Inject constructor(private val localDatabase: LocalDatabase) {
    val pillDao = localDatabase.pillDao();

    suspend fun insertPill(pill: Pill){
        pillDao.insertPill(pill);
    }

    fun getAllPill(): LiveData<List<Pill>>{
        return pillDao.getPillList();
    }

    suspend fun deletePill(pill: Pill){
        pillDao.deletePill(pill);
    }
}