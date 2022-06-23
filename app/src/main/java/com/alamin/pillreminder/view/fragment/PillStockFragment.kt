package com.alamin.pillreminder.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.alamin.pillreminder.R

private const val TAG = "PillStockFragment"
class PillStockFragment : Fragment() {
    private val arg by navArgs<PillStockFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: ${arg.name} ${arg.unit} ${arg.days} ${arg.continuous} ${arg.startDay} ${arg.scheduleList.toList()} ${arg.isEveryDay} ${arg.dayList.toList()} ${arg.dayFrequency}")
        return inflater.inflate(R.layout.fragment_pill_stock, container, false)
    }
}