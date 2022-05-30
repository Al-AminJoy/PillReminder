package com.alamin.pillreminder.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.alamin.pillreminder.R
import com.alamin.pillreminder.databinding.FragmentScheduleBinding

private const val TAG = "ScheduleFragment"
class ScheduleFragment : Fragment() {
    private lateinit var binding: FragmentScheduleBinding
    private val arg by navArgs<ScheduleFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Log.d(TAG, "onCreateView: ${arg.name} ${arg.unit} ${arg.continuous} ${arg.days}" )
        binding = FragmentScheduleBinding.inflate(layoutInflater)

        showInfo()

        return binding.root
    }
    private fun showInfo(){
        val layoutInflater: LayoutInflater=
            requireContext().applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val linearLayout: LinearLayout = binding.infolayout
        var views = ArrayList<View>()

        for (i in 0 until arg.days){
           val view: View = layoutInflater.inflate(R.layout.infoitem, null)
            var tv: TextView = view.findViewById(R.id.TextAddress)
            tv.text = i.toString()
            view.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            views.add(view)
        }

        for(i in 0 until views.size){
            linearLayout.addView(views[i])
        }

    }
}