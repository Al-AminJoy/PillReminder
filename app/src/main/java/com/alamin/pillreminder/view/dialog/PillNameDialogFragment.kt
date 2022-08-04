package com.alamin.pillreminder.view.dialog

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.alamin.pillreminder.PillApplication
import com.alamin.pillreminder.R
import com.alamin.pillreminder.databinding.FragmentPillNameDialogBinding
import com.alamin.pillreminder.utils.DataUtils
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

    private var calender: Calendar = Calendar.getInstance();

    override fun onStart() {
        super.onStart()
        val dialog = dialog;
        dialog?.let {
            // Set Match Parent for Full Screen Dialog
            val width: Int = ViewGroup.LayoutParams.MATCH_PARENT;
            val height: Int = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.window?.setLayout(width, height)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //For Set Round Border
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_background_inset);


        binding = FragmentPillNameDialogBinding.inflate(layoutInflater)
        val component = (requireActivity().applicationContext as PillApplication).appComponent
        component.injectPillName(this)
        pillViewModel =
            ViewModelProvider(this, pillViewModelFactory).get(PillViewModel::class.java);
        binding.pillViewModel = pillViewModel
        binding.lifecycleOwner = this;

        setPillTypeAdapter()
        setUnitAdapter();

        binding.setNextClickListener {
            val pillName = binding.txtPillName.text.toString()
            val pillType = binding.txtPillType.text.toString()
            val pillUnit = binding.txtUnit.text.toString()
            val frequency = binding.txtTakingTime.text.toString()
            val startDate = binding.txtStartDate.text.toString()
            if (pillName.isNotEmpty() && pillUnit.isNotEmpty() && frequency.isNotEmpty() && startDate.isNotEmpty() && pillType.isNotEmpty()) {
                goNext(pillName, pillUnit, frequency, startDate, pillType)

            } else {
                Toast.makeText(
                    requireContext(),
                    "Please Enter All Fields",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.setPillTypeClickListener {
            it.hideKeyboard()
            binding.txtPillType.showDropDown()
        }
        binding.setUnitClickListener {
            it.hideKeyboard()
            binding.txtUnit.showDropDown()
        }

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                calender.set(Calendar.YEAR, year)
                calender.set(Calendar.MONTH, monthOfYear)
                calender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }

        binding.setStartClickListener {
           val datePickerDialog = DatePickerDialog(
                requireContext(),
                dateSetListener,
                calender.get(Calendar.YEAR),
                calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
            datePickerDialog.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.theme_color,null))
            datePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.theme_color,null))
        }

        return binding.root;
    }

    private fun setPillTypeAdapter() {
        val items = DataUtils.pillTypes();
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, R.id.txtItems, items)
        binding.txtPillType.setAdapter(adapter)
    }


    private fun updateDateInView() {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date = simpleDateFormat.format(calender.time)
        binding.txtStartDate.text = Editable.Factory.getInstance().newEditable(date)
    }

    private fun goNext(
        pillName: String,
        pillUnit: String,
        frequency: String,
        startDate: String,
        pillType: String
    ) {
        var action =
            PillNameDialogFragmentDirections.actionPillNameDialogFragmentToScheduleFragment(
                pillName,
                pillUnit,
                frequency,
                startDate,
                pillType
            )
        findNavController().navigate(action)
    }

    fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun setUnitAdapter() {
        val items = DataUtils.pillUnit();
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, R.id.txtItems, items)
        binding.txtUnit.setAdapter(adapter)
    }

}