package com.alamin.pillreminder.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.alamin.pillreminder.R

private const val TAG = "ScheduleFragment"
class ScheduleFragment : Fragment() {
    private val arg by navArgs<ScheduleFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: ${arg.name} ${arg.unit} ${arg.continuous} ${arg.days}" )
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }
}