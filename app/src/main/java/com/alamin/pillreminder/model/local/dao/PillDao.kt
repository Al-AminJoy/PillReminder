package com.alamin.pillreminder.model.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.alamin.pillreminder.model.data.Pill

@Dao
interface PillDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPill(pill: Pill)

    @Update
    suspend fun updatePill(pill: Pill);

    @Delete
    suspend fun deletePill(pill: Pill)

    @Query("SELECT * FROM Pill")
    fun getPillList(): LiveData<List<Pill>>

    @Query("SELECT * FROM Pill WHERE id=:id")
    fun selectPillById(id: Int): LiveData<Pill>
}