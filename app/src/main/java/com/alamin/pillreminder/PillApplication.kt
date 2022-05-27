package com.alamin.pillreminder

import android.app.Application
import com.alamin.pillreminder.di.AppComponent
import com.alamin.pillreminder.di.DaggerAppComponent

class PillApplication : Application() {
    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this);
    }
}