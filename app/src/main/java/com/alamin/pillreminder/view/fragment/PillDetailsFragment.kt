package com.alamin.pillreminder.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.alamin.pillreminder.R
import com.alamin.pillreminder.databinding.FragmentPillDetailsBinding

class PillDetailsFragment : Fragment() {
    private lateinit var binding: FragmentPillDetailsBinding
    private val arg by navArgs<PillDetailsFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //TODO: Create Table For Dose
        binding = FragmentPillDetailsBinding.inflate(layoutInflater)
        binding.pill = arg.pill
        return binding.root
    }

}