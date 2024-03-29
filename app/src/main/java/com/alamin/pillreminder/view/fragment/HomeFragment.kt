package com.alamin.pillreminder.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.alamin.pillreminder.PillApplication
import com.alamin.pillreminder.R
import com.alamin.pillreminder.databinding.FragmentHomeBinding
import com.alamin.pillreminder.model.data.Pill
import com.alamin.pillreminder.view.adapter.RecentPillAdapter
import com.alamin.pillreminder.view.adapter.TodayPillAdapter
import com.alamin.pillreminder.view_model.PillViewModel
import com.alamin.pillreminder.view_model.ViewModelFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import javax.inject.Inject

private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var recentPillAdapter: RecentPillAdapter
    @Inject
    lateinit var todayPillAdapter: TodayPillAdapter
    private lateinit var pillViewModel: PillViewModel
    private lateinit var binding: FragmentHomeBinding;
    private var job: Job? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        val component = (requireActivity().applicationContext as PillApplication).appComponent
        component.injectHome(this)
        pillViewModel = ViewModelProvider(this,viewModelFactory)[PillViewModel::class.java]


        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = todayPillAdapter
        }
        binding.recyclerViewRecentPill.apply {
            layoutManager = GridLayoutManager(requireContext(),2)
            adapter = recentPillAdapter
        }


        pillViewModel.getAllPill().observe(requireActivity(), Observer {
            job?.cancel()
            job = CoroutineScope(IO).launch {
                while (true){
                    showData(pillViewModel.getTodayPill(it))
                    delay(1000*10)
                }
            }
        })

        binding.setFloatingClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_pillNameDialogFragment)
        }

        return binding.root
    }

    private suspend fun showData(todayPill: List<Pill>) {
        withContext(Main){
            if (todayPill.isEmpty()){
                binding.txtToday.text = "Woo !! No Pill in Today"
                binding.cardRecentPill.visibility = View.GONE
            }else{
                todayPillAdapter.setData(todayPill)
                withContext(IO){
                    val recentPill = pillViewModel.getRecentPillData(todayPill)
                    withContext(Main){
                        binding.txtRecentPill.text = if (recentPill.isEmpty()) "Woo !! No Upcoming Pill" else "Upcoming Medicine(s)"
                        recentPillAdapter.setData(recentPill);
                }
            }
        }
    }}

}


