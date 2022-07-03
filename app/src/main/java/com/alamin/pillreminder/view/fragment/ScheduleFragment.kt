package com.alamin.pillreminder.view.fragment

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alamin.pillreminder.PillApplication
import com.alamin.pillreminder.R
import com.alamin.pillreminder.databinding.FragmentScheduleBinding
import com.alamin.pillreminder.databinding.NumberPickerLayoutBinding
import com.alamin.pillreminder.databinding.SpecificDayLayoutBinding
import com.alamin.pillreminder.model.data.DayHolder
import com.alamin.pillreminder.model.data.Pill
import com.alamin.pillreminder.model.data.Schedule
import com.alamin.pillreminder.model.data.ScheduleHolder
import com.alamin.pillreminder.view_model.PillViewModel
import com.alamin.pillreminder.view_model.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_schedule.*
import java.text.SimpleDateFormat
import javax.inject.Inject


private const val TAG = "ScheduleFragment"
class ScheduleFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory;

    private lateinit var pillViewModel: PillViewModel;

    private lateinit var binding: FragmentScheduleBinding
    private val arg by navArgs<ScheduleFragmentArgs>()
    private lateinit var dayList : ArrayList<String>
    private lateinit var views: ArrayList<View>
    private lateinit var scheduleList: ArrayList<Schedule>
    private var isEveryDay: Boolean = true
    private var isEvery: Boolean = false
    private var every = 0;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScheduleBinding.inflate(layoutInflater)
        binding.isEvery = isEvery
        val component = (requireActivity().applicationContext as PillApplication).appComponent
        component.injectSchedule(this);
        pillViewModel = ViewModelProvider(this,viewModelFactory)[PillViewModel::class.java];

        dayList = arrayListOf()
        scheduleList = arrayListOf()
        showInfo()

        binding.setOnEveryDayClick {
            isEvery = false
            binding.isEvery = isEvery
            isEveryDay = true
            dayList.clear()
            txtDays.text = ""
        }
        binding.setOnSpecificDayClick {
            dayList.clear()
            txtDays.text = ""
            isEvery = false
            binding.isEvery = isEvery
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
                    every = newValue
                    binding.txtDays.text = "$every Day's"
                    isEvery = true
                    binding.isEvery = isEvery
                }
            }
            dialogBinding.setOnNumberSubmit {
                every = dialogBinding.numberPicker.value
                binding.txtDays.text = "$every Day's"
                isEvery = true
                binding.isEvery = isEvery
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
                val txtDosage: EditText = item.findViewById(R.id.txtDosages)
                val radioGroup: RadioGroup = item.findViewById(R.id.radioGroup)
                val btnBeforeMeal: RadioButton = item.findViewById(R.id.btnBeforeMeal)
                val btnAfterMeal: RadioButton = item.findViewById(R.id.btnAfterMeal)
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
                var mealStatus = ""

                val selectedId = radioGroup.checkedRadioButtonId

                if (selectedId == -1){
                    Toast.makeText(requireContext(),"Please Select Meal",Toast.LENGTH_SHORT).show()
                    return@setOnNext
                }else {
                    val radioButton = radioGroup.findViewById(selectedId) as RadioButton
                    mealStatus = radioButton.text.toString()
                }

                scheduleList.add(Schedule(time,mealStatus,dose.toDouble()))
            }
            val dayHolder = DayHolder(dayList)
            val scheduleHolder =  ScheduleHolder(scheduleList)
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            var startTime: Long = 0
            try {
                val date = simpleDateFormat.parse(arg.startDay)
                startTime = date.time;
            }catch (e: Exception){

            }

            if (isEvery){
                insertPill(arg.name, arg.pillType, arg.unit,startTime, arg.continuous, arg.days, isEveryDay, dayHolder, every, scheduleHolder)
            }else {
                every = 0
                insertPill(arg.name, arg.pillType, arg.unit, startTime, arg.continuous, arg.days, isEveryDay, dayHolder, every, scheduleHolder)
            }

        }

        return binding.root
    }


    private fun insertPill(
        name: String,
        type: String,
        unit: String,
        startDay: Long,
        continuous: Boolean,
        days: Int,
        everyDay: Boolean,
        dayHolder: DayHolder,
        every: Int,
        scheduleHolder: ScheduleHolder
    ) {
        val pill = Pill(0,name,type,unit,startDay,continuous,days,everyDay,dayHolder,every,scheduleHolder)
        pillViewModel.insertPill(pill)
        findNavController().navigate(R.id.action_scheduleFragment_to_homeFragment)
    }


    private fun showInfo(){
        val layoutInflater: LayoutInflater= requireActivity().applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

         views = arrayListOf()

        for (item in 0 until arg.frequency.toInt()){
           val view: View = layoutInflater.inflate(R.layout.frequency_item, null)
            var txtTime: AutoCompleteTextView = view.findViewById(R.id.txtTime)
            var txtDosageTitle: TextView = view.findViewById(R.id.txtDosageTitle)
            var txtMedicineUnit: TextView = view.findViewById(R.id.txtMedicineUnit)

            txtDosageTitle.text = "Dosage : ${item+1}"
            txtMedicineUnit.text = "${arg.unit}(s)"

            val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
                TimePickerDialog.OnTimeSetListener { view, hour, minute ->
                    val formattedtime: String = when{
                        hour == 0 ->{
                            if (minute < 10){
                                "12:0$minute AM"
                            }else{
                                "12:$minute AM"
                            }
                        }

                        hour > 12 ->{
                            if (minute < 10){
                                "${hour-12}:0$minute PM"
                            }else{
                                "${hour-12}:$minute PM"
                            }
                        }

                        hour == 12 ->{
                            if (minute < 10){
                                "$hour:0$minute PM"
                            }else{
                                "$hour:$minute PM"
                            }
                        }

                        else-> {
                            if (minute < 10){
                                "$hour:0$minute AM"
                            }else{
                                "$hour:$minute AM"
                            }
                        }
                    }
                    txtTime.text  = Editable.Factory.getInstance().newEditable(formattedtime)
                }

            txtTime.setOnClickListener {
                val timePicker: TimePickerDialog = TimePickerDialog(requireContext(),timePickerDialogListener,12,10,false)
                timePicker.show()
                timePicker.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.theme_color,null))
                timePicker.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.theme_color,null))
            }

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