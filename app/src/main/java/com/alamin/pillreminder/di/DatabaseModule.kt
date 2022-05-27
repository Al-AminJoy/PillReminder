package com.alamin.pillreminder.di

import android.content.Context
import androidx.room.Room
import com.alamin.pillreminder.model.local.database.LocalDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideLocalDatabase(context: Context): LocalDatabase{
        return Room.databaseBuilder(
            context.applicationContext,
            LocalDatabase::class.java,
            "local_database")
            .build();
    }
}