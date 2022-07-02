package com.alamin.pillreminder.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.alamin.pillreminder.PillApplication
import com.alamin.pillreminder.R
import com.alamin.pillreminder.databinding.FragmentPillListBinding
import com.alamin.pillreminder.model.data.Pill
import com.alamin.pillreminder.view.adapter.OnPillListClickListener
import com.alamin.pillreminder.view.adapter.PillAdapter
import com.alamin.pillreminder.view_model.PillViewModel
import com.alamin.pillreminder.view_model.ViewModelFactory
import javax.inject.Inject
import kotlin.math.log

private const val TAG = "PillListFragment"
class PillListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var pillAdapter: PillAdapter

    private lateinit var pillViewModel: PillViewModel

    private lateinit var binding: FragmentPillListBinding;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPillListBinding.inflate(layoutInflater)
        val component = (requireActivity().applicationContext as PillApplication).appComponent
        component.injectPillList(this)
        pillViewModel = ViewModelProvider(this,viewModelFactory)[PillViewModel::class.java]

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(),3)
            adapter = pillAdapter
        }

        pillViewModel.getAllPill().observe(requireActivity(), Observer {
            with(pillAdapter){
                setData(it);
                setOnClickListener(object : OnPillListClickListener{
                    override fun onClick(pill: Pill) {
                        val component = PillListFragmentDirections.actionPillListFragmentToPillDetailsFragment(pill)
                        findNavController().navigate(component)
                    }

                })
            }
        })



        return binding.root
    }
}