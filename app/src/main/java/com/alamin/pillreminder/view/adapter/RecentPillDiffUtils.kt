package com.alamin.pillreminder.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.alamin.pillreminder.model.data.RecentSchedule
import javax.inject.Inject

class RecentPillDiffUtils @Inject constructor() :
    DiffUtil.Callback(){
    private lateinit var oldList: List<RecentSchedule>
    private lateinit var newList: List<RecentSchedule>

    fun setList(oldList: List<RecentSchedule>, newList: List<RecentSchedule>){
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
        return oldList[oldItemPosition].pillId == newList[newItemPosition].pillId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when{
            oldList[oldItemPosition].pillId != newList[newItemPosition].pillId ->false
            oldList[oldItemPosition].pillName != newList[newItemPosition].pillName ->false
            oldList[oldItemPosition].pillUnit != newList[newItemPosition].pillUnit ->false
            oldList[oldItemPosition].time != newList[newItemPosition].time -> false
            oldList[oldItemPosition].unit != newList[newItemPosition].unit -> false
            else -> true
        }
    }
}