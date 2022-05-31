package com.alamin.pillreminder.view.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.alamin.pillreminder.R
import com.alamin.pillreminder.databinding.FragmentScheduleBinding
import com.alamin.pillreminder.utils.DataUtils

private const val TAG = "ScheduleFragment"
class ScheduleFragment : Fragment() {
    private lateinit var binding: FragmentScheduleBinding
    private val arg by navArgs<ScheduleFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScheduleBinding.inflate(layoutInflater)

        showInfo()

        return binding.root
    }
    private fun showInfo(){
        val layoutInflater: LayoutInflater= requireContext().applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        var views = ArrayList<View>()

        for (item in 0 until arg.frequency.toInt()){
           val view: View = layoutInflater.inflate(R.layout.frequency_item, null)
            var txtTime: AutoCompleteTextView = view.findViewById(R.id.txtTime)
            var txtDosage: AutoCompleteTextView = view.findViewById(R.id.txtDosages)
            var txtDosageTitle: TextView = view.findViewById(R.id.txtDosageTitle)

            txtDosageTitle.text = "Dosage : ${item+1}"

            txtDosage.setOnClickListener {
                txtDosage.showDropDown()
            }
            val  dosagesAdapter = ArrayAdapter(requireContext(),R.layout.list_item,R.id.txtItems,DataUtils.pillDosages(arg.unit))
            txtDosage.setAdapter(dosagesAdapter)

            txtTime.setOnClickListener {
                txtTime.showDropDown()
            }
            val  timeAdapter = ArrayAdapter(requireContext(),R.layout.list_item,R.id.txtItems,DataUtils.pillTimes())
            txtTime.setAdapter(timeAdapter)


            var layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(0,32,0,0)

            view.layoutParams = layoutParams

            views.add(view)
        }

        for(item in 0 until views.size){
            binding.layoutFrequency.addView(views[item])
        }

    }
}