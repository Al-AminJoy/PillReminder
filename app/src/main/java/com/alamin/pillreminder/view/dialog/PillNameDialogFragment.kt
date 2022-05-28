package com.alamin.pillreminder.view.dialog

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.alamin.pillreminder.R


class PillNameDialogFragment : DialogFragment() {

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

        return inflater.inflate(R.layout.fragment_pill_name_dialog, container, false)
    }

}