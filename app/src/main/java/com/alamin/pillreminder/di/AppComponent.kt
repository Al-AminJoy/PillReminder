package com.alamin.pillreminder.di

import android.content.Context
import androidx.lifecycle.ViewModel
import com.alamin.pillreminder.view.activity.MainActivity
import com.alamin.pillreminder.view.dialog.PillNameDialogFragment
import com.alamin.pillreminder.view.fragment.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class, ViewModelModule::class])
interface AppComponent {
    fun injectMain(mainActivity: MainActivity)

    fun injectHome(homeFragment: HomeFragment)

    fun injectProfile(profileFragment: ProfileFragment)

    fun injectPillList(pillListFragment: PillListFragment)

    fun injectRemind(reminderFragment: ReminderFragment)

    fun injectSchedule(scheduleFragment: ScheduleFragment)

    fun injectPillName(pillNameDialogFragment: PillNameDialogFragment)

    fun injectPillDetails(pillDetailsFragment: PillDetailsFragment)

    fun getViewModelMap(): Map<Class<*>,ViewModel>

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): AppComponent
    }
}