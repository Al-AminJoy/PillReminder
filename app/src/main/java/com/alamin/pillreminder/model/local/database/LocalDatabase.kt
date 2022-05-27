package com.alamin.pillreminder.model.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alamin.pillreminder.model.data.Pill
import com.alamin.pillreminder.model.local.dao.PillDao

@Database(entities = [Pill::class], version = 1, exportSchema = true)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun pillDao() : PillDao
}