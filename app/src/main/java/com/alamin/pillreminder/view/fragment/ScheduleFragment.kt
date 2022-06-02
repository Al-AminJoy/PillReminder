package com.alamin.pillreminder.view.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.alamin.pillreminder.R
import com.alamin.pillreminder.databinding.FragmentScheduleBinding
import com.alamin.pillreminder.databinding.NumberPickerLayoutBinding
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

        binding.setOnEveryDayClick {
            Toast.makeText(requireContext(), "EveryDay", Toast.LENGTH_SHORT).show()
        }
        binding.setOnSpecificDayClick {
            Toast.makeText(requireContext(), "SpecificDay", Toast.LENGTH_SHORT).show()
        }
        binding.setOnEveryClick {
            val alertDialog = Dialog(requireContext())
            val dialogBinding = NumberPickerLayoutBinding.inflate(LayoutInflater.from(requireContext()))
            dialogBinding.numberPicker.minValue = 2
            dialogBinding.numberPicker.maxValue = 15
            dialogBinding.setOnNumberChange { numberPicker, oldValue, newValue ->
                run {
                    val text = "Changed from " + oldValue.toString() + " to " + newValue
                    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()

                }
            }
            alertDialog.setContentView(dialogBinding.root)
            alertDialog.show()
        }

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
            var txtMedicineUnit: TextView = view.findViewById(R.id.txtMedicineUnit)

            txtDosageTitle.text = "Dosage : ${item+1}"
            txtMedicineUnit.text = "${arg.unit}(s)"

            txtDosage.setOnClickListener {
                txtDosage.showDropDown()
            }
            val  dosagesAdapter = ArrayAdapter(requireContext(),R.layout.list_item,R.id.txtItems,DataUtils.pillDosages())
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