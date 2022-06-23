package com.alamin.pillreminder.view.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alamin.pillreminder.R
import com.alamin.pillreminder.databinding.FragmentScheduleBinding
import com.alamin.pillreminder.databinding.NumberPickerLayoutBinding
import com.alamin.pillreminder.databinding.SpecificDayLayoutBinding
import com.alamin.pillreminder.model.data.Schedule
import com.alamin.pillreminder.utils.DataUtils
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.fragment_schedule.view.*


private const val TAG = "ScheduleFragment"
class ScheduleFragment : Fragment() {
    private lateinit var binding: FragmentScheduleBinding
    private val arg by navArgs<ScheduleFragmentArgs>()
    private lateinit var dayList : ArrayList<String>
    private lateinit var views: ArrayList<View>
    private lateinit var scheduleList: ArrayList<Schedule>
    private var isEveryDay: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScheduleBinding.inflate(layoutInflater)
        binding.isEvery = false
        dayList = arrayListOf()
        scheduleList = arrayListOf()
        showInfo()

        binding.setOnEveryDayClick {
            binding.isEvery = false
            isEveryDay = true
            dayList.clear()
            txtDays.text = ""
        }
        binding.setOnSpecificDayClick {
            dayList.clear()
            txtDays.text = ""
            binding.isEvery = false
            isEveryDay = false
            val dialog = Dialog(requireContext())
            val dialogBinding = SpecificDayLayoutBinding.inflate(LayoutInflater.from(requireContext()))
            dialogBinding.setOnSubmit {
                if (dialogBinding.checkSaturday.isChecked){
                    dayList.add("Saturday")
                }else{
                    dayList.remove("Saturday")
                }

                if (dialogBinding.checkSunday.isChecked){
                dayList.add("Sunday")
                }else{
                    dayList.remove("Sunday")
                }

                if (dialogBinding.checkMonday.isChecked){
                    dayList.add("Monday")
                }else{
                    dayList.remove("Monday")
                }

                if (dialogBinding.checkTuesday.isChecked){
                    dayList.add("Tuesday")
                }else{
                    dayList.remove("Tuesday")
                }

                if (dialogBinding.checkWednesday.isChecked){
                    dayList.add("Wednesday")
                }else{
                    dayList.remove("Wednesday")
                }

                if (dialogBinding.checkThursday.isChecked){
                    dayList.add("Thursday")
                }else{
                    dayList.remove("Thursday")
                }

                if (dialogBinding.checkFriday.isChecked){
                    dayList.add("Friday")
                }else{
                    dayList.remove("Friday")
                }
                if (dayList.size == 0){
                    Toast.makeText(
                        requireContext(),
                        "Please Select At Least One Day",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnSubmit
                }
                dialog.dismiss()
            }
            dialog.setContentView(dialogBinding.root)
            dialog.setCancelable(false)
            dialog.show()
            val window = dialog.window
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)

        }
        binding.setOnEveryClick {
            isEveryDay = false
            dayList.clear()
            val alertDialog = Dialog(requireContext())
            val dialogBinding = NumberPickerLayoutBinding.inflate(LayoutInflater.from(requireContext()))
            dialogBinding.numberPicker.minValue = 2
            dialogBinding.numberPicker.maxValue = 15
            dialogBinding.setOnNumberChange { numberPicker, oldValue, newValue ->
                run {
                    binding.txtDays.text = "$newValue Day's"
                    binding.isEvery = true
                }
            }
            dialogBinding.setOnNumberSubmit {
                binding.txtDays.text = "${dialogBinding.numberPicker.value} Day's"
                binding.isEvery = true
                alertDialog.dismiss()

            }
            alertDialog.setContentView(dialogBinding.root)
            alertDialog.setCancelable(false)
            alertDialog.show()
            val window: Window? = alertDialog.window
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }


        binding.setOnNext {
            scheduleList.clear()
            for (item in views){
                val txtTime: AutoCompleteTextView = item.findViewById(R.id.txtTime)
                val txtDosage: AutoCompleteTextView = item.findViewById(R.id.txtDosages)
                txtTime.error = null
                txtDosage.error = null

                val time = txtTime.text.toString()
                val dose = txtDosage.text.toString()
                if (time.isEmpty()){
                    txtTime.error = "Select Time"
                    txtTime.focusable
                    scheduleList.clear()
                    return@setOnNext
                }
                if (dose.isEmpty()){
                    txtDosage.error = "Select Dose"
                    txtDosage.focusable
                    scheduleList.clear()
                    return@setOnNext
                }
                scheduleList.add(Schedule(time,dose.toDouble()))
            }
            Log.d(TAG, "onCreateView: ${scheduleList.size} $scheduleList")
            Log.d(TAG, "onCreateView:  $dayList")
            val action = ScheduleFragmentDirections.actionScheduleFragmentToPillStockFragment(arg.name,
                arg.unit,
                arg.days,
                arg.continuous,
                arg.startDay,
            scheduleList.toTypedArray(),
            isEveryDay,
            dayList.toTypedArray(),
            binding.txtDays.text.toString())
            findNavController().navigate(action)
        }

        return binding.root
    }


    private fun showInfo(){
        val layoutInflater: LayoutInflater= requireContext().applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

         views = arrayListOf()

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