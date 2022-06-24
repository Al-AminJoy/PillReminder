package com.alamin.pillreminder.di

import androidx.lifecycle.ViewModel
import com.alamin.pillreminder.view_model.PillViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @ClassKey(PillViewModel::class)
    @IntoMap
    abstract fun provideUserViewModel(pillViewModel: PillViewModel): ViewModel
}