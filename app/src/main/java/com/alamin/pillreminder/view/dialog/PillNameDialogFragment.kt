package com.alamin.pillreminder.view.dialog

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
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
            if (pillName.isNotEmpty() && pillUnit.isNotEmpty()){
                var action = PillNameDialogFragmentDirections.actionPillNameDialogFragmentToReminderFragment(pillName,pillUnit)
                findNavController().navigate(action)
            }
        }

        binding.setUnitClickListener {
            binding.txtUnit.showDropDown()
        }

        binding.radioGroupSchedule.setOnCheckedChangeListener { group, checkedId ->
            run {
                when (checkedId) {
                    R.id.btnContinuous -> {
                        isContinuous = true
                    }
                    R.id.btnNumberOfDays -> {
                        isContinuous = false;
                    }
                }
                pillViewModel.setContinuous(isContinuous)
            }
        }
        return binding.root;
    }

    private fun setUnitAdapter() {
        val dataUtils = DataUtils();
        val items = dataUtils.pillTypes();
        val adapter = ArrayAdapter(requireContext(),R.layout.list_item,R.id.txtItems,items)
        binding.txtUnit.setAdapter(adapter)
    }

}