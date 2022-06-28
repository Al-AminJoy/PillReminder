package com.alamin.pillreminder.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alamin.pillreminder.databinding.RowTodayPillBinding
import com.alamin.pillreminder.model.data.Pill
import javax.inject.Inject

class TodayPillAdapter @Inject constructor(val pillDiffUtils: PillDiffUtils):
    RecyclerView.Adapter<TodayPillAdapter.TodayPillViewHolder>(){

    private var pillList: List<Pill> = arrayListOf()

    inner class TodayPillViewHolder(private val binding: RowTodayPillBinding): RecyclerView.ViewHolder(binding.root) {

        fun binding(pill: Pill){
            binding.pill = pill
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
        return pillList.size
    }

    fun setData(newPillList: List<Pill>){
        pillDiffUtils.setList(pillList,newPillList)
        val diffResult = DiffUtil.calculateDiff(pillDiffUtils)
        pillList = newPillList
        diffResult.dispatchUpdatesTo(this)
    }
}