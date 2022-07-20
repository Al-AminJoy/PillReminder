package com.alamin.pillreminder.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alamin.pillreminder.PillApplication
import com.alamin.pillreminder.R
import com.alamin.pillreminder.databinding.FragmentPillDetailsBinding
import com.alamin.pillreminder.view_model.PillViewModel
import com.alamin.pillreminder.view_model.ViewModelFactory
import javax.inject.Inject

class PillDetailsFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: PillViewModel;
    private lateinit var binding: FragmentPillDetailsBinding
    private val arg by navArgs<PillDetailsFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPillDetailsBinding.inflate(layoutInflater)
        val component = (requireActivity().applicationContext as PillApplication).appComponent
        component.injectPillDetails(this);
        viewModel = ViewModelProvider(this,viewModelFactory)[PillViewModel::class.java]
        binding.pill = arg.pill
        binding.setOnDeleteClick {
            viewModel.deletePill(arg.pill)
            findNavController().navigate(R.id.action_pillDetailsFragment_to_pillListFragment)
        }
        return binding.root
    }

}