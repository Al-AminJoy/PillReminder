package com.alamin.pillreminder.di

import android.content.Context
import androidx.lifecycle.ViewModel
import com.alamin.pillreminder.view.activity.MainActivity
import com.alamin.pillreminder.view.fragment.CreatePillFragment
import com.alamin.pillreminder.view.fragment.HomeFragment
import com.alamin.pillreminder.view.fragment.PillListFragment
import com.alamin.pillreminder.view.fragment.ProfileFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class, ViewModelModule::class])
interface AppComponent {
    fun injectMain(mainActivity: MainActivity)
    fun injectHome(homeFragment: HomeFragment)
    fun injectProfile(profileFragment: ProfileFragment)
    fun injectCreatePill(createPillFragment: CreatePillFragment)
    fun injectPillList(pillListFragment: PillListFragment)

    fun getViewModelMap(): Map<Class<*>,ViewModel>

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): AppComponent
    }
}