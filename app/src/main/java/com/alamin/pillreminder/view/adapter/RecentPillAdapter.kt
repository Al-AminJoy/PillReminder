package com.alamin.pillreminder.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alamin.pillreminder.databinding.RowRecentPillBinding
import com.alamin.pillreminder.model.data.RecentSchedule
import javax.inject.Inject

private const val TAG = "TodayPillAdapter"
class RecentPillAdapter @Inject constructor(private val recentPillDiffUtils: RecentPillDiffUtils):
    RecyclerView.Adapter<RecentPillAdapter.RecentPillViewHolder>(){

    private var pillList : List<RecentSchedule> = arrayListOf()

    inner class RecentPillViewHolder(val binding: RowRecentPillBinding): RecyclerView.ViewHolder(binding.root) {
        fun binding(recentSchedule: RecentSchedule){
            binding.schedule = recentSchedule
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecentPillAdapter.RecentPillViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RecentPillViewHolder(RowRecentPillBinding.inflate(inflater,parent,false))
    }

    override fun onBindViewHolder(holder: RecentPillAdapter.RecentPillViewHolder, position: Int) {
        holder.binding(pillList[position])
    }

    override fun getItemCount(): Int {
       return pillList.size
    }

    fun setData(newPillList: List<RecentSchedule>){
        recentPillDiffUtils.setList(pillList,newPillList)
        val diffResult = DiffUtil.calculateDiff(recentPillDiffUtils)
        pillList = newPillList
        diffResult.dispatchUpdatesTo(this)
    }

}