package com.alamin.pillreminder.view.dialog

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
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
import javax.inject.Inject

private const val TAG = "PillNameDialogFragment"
class PillNameDialogFragment : DialogFragment() {

    @Inject
    lateinit var pillViewModelFactory: ViewModelFactory;

    private lateinit var pillViewModel: PillViewModel;

    private lateinit var binding: FragmentPillNameDialogBinding
    private var isContinuous: Boolean = true;
    private var days = 0;

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
            val frequency = binding.txtTakingTime.text.toString()
            if (pillName.isNotEmpty() && pillUnit.isNotEmpty() && frequency.isNotEmpty()){
                if (isContinuous){
                    days = 0;
                    goNext(pillName,pillUnit,frequency,days,isContinuous)
                }else  {
                    if (binding.txtDays.text.toString().isNotEmpty()){
                        days = binding.txtDays.text.toString().toInt()
                        goNext(pillName,pillUnit,frequency,days,isContinuous)
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

        return binding.root;
    }

    private fun goNext(pillName: String, pillUnit: String,frequency: String, days: Int, continuous: Boolean) {
        var action = PillNameDialogFragmentDirections.actionPillNameDialogFragmentToScheduleFragment(pillName,pillUnit,frequency,days,continuous)
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