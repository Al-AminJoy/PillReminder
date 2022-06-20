package com.alamin.pillreminder.view.dialog

import android.app.DatePickerDialog
import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.alamin.pillreminder.PillApplication
import com.alamin.pillreminder.R
import com.alamin.pillreminder.databinding.FragmentPillNameDialogBinding
import com.alamin.pillreminder.utils.DataUtils
import com.alamin.pillreminder.utils.PillCreator
import com.alamin.pillreminder.view_model.PillViewModel
import com.alamin.pillreminder.view_model.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val TAG = "PillNameDialogFragment"
class PillNameDialogFragment : DialogFragment() {

    @Inject
    lateinit var pillViewModelFactory: ViewModelFactory;

    private lateinit var pillViewModel: PillViewModel;

    private lateinit var binding: FragmentPillNameDialogBinding
    private var isContinuous: Boolean = true;
    private var days = 0;

    private var calender: Calendar = Calendar.getInstance();

    override fun onStart() {
        super.onStart()
        val dialog = dialog;
        dialog?.let {
            // Set Match Parent for Full Screen Dialog
            val width: Int = ViewGroup.LayoutParams.MATCH_PARENT;
            val height: Int = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.window?.setLayout(width,height)
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //For Set Round Border
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_background_inset);


        binding =  FragmentPillNameDialogBinding.inflate(layoutInflater)
        val component = (requireActivity().applicationContext as PillApplication).appComponent
        component.injectPillName(this)
        pillViewModel = ViewModelProvider(this,pillViewModelFactory).get(PillViewModel::class.java);
        pillViewModel.setContinuous(isContinuous)
        binding.pillViewModel = pillViewModel
        binding.lifecycleOwner = this;
        
        setUnitAdapter();

        binding.setNextClickListener {
            val pillName = binding.txtPillName.text.toString()
            val pillUnit = binding.txtUnit.text.toString()
            val pillDate = binding.txtStartDate.text.toString()
            val frequency = binding.txtTakingTime.text.toString()
            if (pillName.isNotEmpty() && pillUnit.isNotEmpty() && frequency.isNotEmpty() && pillDate.isNotEmpty()){
                if (isContinuous){
                    days = 0;
                    goNext(pillName,pillUnit,pillDate,frequency,days,isContinuous)
                }else  {
                    if (binding.txtDays.text.toString().isNotEmpty()){
                        days = binding.txtDays.text.toString().toInt()
                        goNext(pillName,pillUnit,pillDate,frequency,days,isContinuous)
                    } else{
                        Toast.makeText(requireContext(),"Please Set Day",Toast.LENGTH_SHORT).show()
                    }
                }
            } else{
                Toast.makeText(requireContext(),"Please Enter Medicine Name & Unit",Toast.LENGTH_SHORT).show()
            }
        }

        binding.setUnitClickListener {
            it.hideKeyboard()
            binding.txtUnit.showDropDown()
        }

        binding.setContinuousClickListener {
            isContinuous = true
            pillViewModel.setContinuous(isContinuous)
        }

        binding.setNumberOfDaysClickListener {
            isContinuous = false;
            pillViewModel.setContinuous(isContinuous)
        }

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calender.set(Calendar.YEAR,year)
            calender.set(Calendar.MONTH,monthOfYear)
            calender.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            updateDateInView()
        }

        binding.setStartClickListener {
            DatePickerDialog(requireContext(),
            dateSetListener,
            calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH))
                .show()

        }

        return binding.root;
    }

    private fun updateDateInView() {
        var day = calender.get(Calendar.DAY_OF_MONTH)
        var month: Int = calender.get(Calendar.MONTH)
        var year: Int = calender.get(Calendar.YEAR)
        val date = "$day/$month/$year"
        binding.txtStartDate.text = Editable.Factory.getInstance().newEditable(date)
    }

    private fun goNext(pillName: String, pillUnit: String,date: String,frequency: String, days: Int, continuous: Boolean) {
        var action = PillNameDialogFragmentDirections.actionPillNameDialogFragmentToScheduleFragment(pillName,pillUnit,date,frequency,days,continuous)
        findNavController().navigate(action)
    }

    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun setUnitAdapter() {
        val items = DataUtils.pillTypes();
        val adapter = ArrayAdapter(requireContext(),R.layout.list_item,R.id.txtItems,items)
        binding.txtUnit.setAdapter(adapter)
    }

}