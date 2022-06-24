package com.alamin.pillreminder.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alamin.pillreminder.model.data.Pill
import com.alamin.pillreminder.model.repository.PillRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class PillViewModel @Inject constructor(private val pillRepository: PillRepository) : ViewModel() {
     val isContinuous = MutableLiveData<Boolean>();

    fun setContinuous(continuous: Boolean){
        isContinuous.value = continuous
    }

    fun insertPill(pill: Pill){
        viewModelScope.launch(Dispatchers.IO) {
            pillRepository.insertPill(pill)
        }
    }

    fun getAllPill(): LiveData<List<Pill>>{
        return pillRepository.getAllPill();
    }
}