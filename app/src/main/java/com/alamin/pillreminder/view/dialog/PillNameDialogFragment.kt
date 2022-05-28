package com.alamin.pillreminder.view.dialog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.alamin.pillreminder.R
import com.alamin.pillreminder.databinding.FragmentPillNameDialogBinding
import com.alamin.pillreminder.utils.PillCreator

private const val TAG = "PillNameDialogFragment"
class PillNameDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentPillNameDialogBinding


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

        binding.setNextClickListener {
            val pillName = binding.txtPillName.text.toString()
            val pillUnit = binding.txtUnit.text.toString()
            if (pillName.isNotEmpty() && pillUnit.isNotEmpty()){
                var action = PillNameDialogFragmentDirections.actionPillNameDialogFragmentToReminderFragment(pillName,pillUnit)
                findNavController().navigate(action)
            }
        }

        return binding.root;
    }

}