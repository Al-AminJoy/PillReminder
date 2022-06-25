package com.alamin.pillreminder.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alamin.pillreminder.databinding.FragmentPillListBinding
import com.alamin.pillreminder.databinding.RowPillItemBinding
import com.alamin.pillreminder.model.data.Pill
import javax.inject.Inject

private const val TAG = "PillAdapter"
class PillAdapter @Inject constructor(private val pillDiffUtils: PillDiffUtils) :
    RecyclerView.Adapter<PillAdapter.PillViewHolder>(){

    private var pillList: List<Pill> = arrayListOf()

    inner class PillViewHolder (val binding: RowPillItemBinding) :
    RecyclerView.ViewHolder(binding.root){
        fun binding(pill: Pill){
            binding.pill = pill
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PillAdapter.PillViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PillViewHolder(RowPillItemBinding.inflate(inflater,parent,false))
    }

    override fun onBindViewHolder(holder: PillAdapter.PillViewHolder, position: Int) {
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