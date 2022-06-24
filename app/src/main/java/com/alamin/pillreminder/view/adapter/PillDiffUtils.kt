package com.alamin.pillreminder.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.alamin.pillreminder.model.data.Pill
import javax.inject.Inject

class PillDiffUtils @Inject constructor() :
    DiffUtil.Callback(){

    private lateinit var oldList: List<Pill>
    private lateinit var newList: List<Pill>

    fun setList(oldList: List<Pill>, newList: List<Pill>){
        this.oldList = oldList
        this.newList = newList
    }

    override fun getOldListSize(): Int {
       return oldList.size
    }

    override fun getNewListSize(): Int {
       return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when{
            oldList[oldItemPosition].id != newList[newItemPosition].id ->false
            oldList[oldItemPosition].pillName != newList[newItemPosition].pillName ->false
            oldList[oldItemPosition].pillUnit != newList[newItemPosition].pillUnit ->false
            oldList[oldItemPosition].pillStartTime != newList[newItemPosition].pillStartTime ->false
            oldList[oldItemPosition].isContinuous != newList[newItemPosition].isContinuous ->false
            oldList[oldItemPosition].days != newList[newItemPosition].days ->false
            oldList[oldItemPosition].isEveryDay != newList[newItemPosition].isEveryDay ->false
            oldList[oldItemPosition].dayHolder != newList[newItemPosition].dayHolder ->false
            oldList[oldItemPosition].dayInterval != newList[newItemPosition].dayInterval ->false
            oldList[oldItemPosition].scheduleHolder != newList[newItemPosition].scheduleHolder ->false
            else -> true
        }
    }
}