package com.alamin.pillreminder.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alamin.pillreminder.databinding.RowTodayPillBinding
import com.alamin.pillreminder.model.data.RecentSchedule
import javax.inject.Inject

private const val TAG = "TodayPillAdapter"
class TodayPillAdapter @Inject constructor(private val recentPillDiffUtils: RecentPillDiffUtils):
    RecyclerView.Adapter<TodayPillAdapter.TodayPillViewHolder>(){

    private var pillList : List<RecentSchedule> = arrayListOf()

    inner class TodayPillViewHolder(val binding: RowTodayPillBinding): RecyclerView.ViewHolder(binding.root) {
        fun binding(recentSchedule: RecentSchedule){
            binding.schedule = recentSchedule
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TodayPillAdapter.TodayPillViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TodayPillViewHolder(RowTodayPillBinding.inflate(inflater,parent,false))
    }

    override fun onBindViewHolder(holder: TodayPillAdapter.TodayPillViewHolder, position: Int) {
        holder.binding(pillList[position])
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: ${pillList.size}")
       return pillList.size
    }

    fun setData(newPillList: List<RecentSchedule>){
        Log.d(TAG, "New List: ${pillList.size}")

        recentPillDiffUtils.setList(pillList,newPillList)
        val diffResult = DiffUtil.calculateDiff(recentPillDiffUtils)
        pillList = newPillList
        diffResult.dispatchUpdatesTo(this)
    }

}