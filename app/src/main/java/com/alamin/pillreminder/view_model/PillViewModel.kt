package com.alamin.pillreminder.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alamin.pillreminder.model.repository.PillRepository
import javax.inject.Inject

class PillViewModel @Inject constructor(private val pillRepository: PillRepository) : ViewModel() {
     val isContinuous = MutableLiveData<Boolean>();

    fun setContinuous(continuous: Boolean){
        isContinuous.value = continuous
    }
}