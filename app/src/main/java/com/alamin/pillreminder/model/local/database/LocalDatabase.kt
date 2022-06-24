package com.alamin.pillreminder.model.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alamin.pillreminder.model.data.Converters
import com.alamin.pillreminder.model.data.Pill
import com.alamin.pillreminder.model.local.dao.PillDao

@Database(entities = [Pill::class], version = 5, exportSchema = true)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun pillDao() : PillDao
}