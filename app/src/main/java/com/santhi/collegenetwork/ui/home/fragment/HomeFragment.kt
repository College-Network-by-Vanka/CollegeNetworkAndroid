package com.santhi.collegenetwork.ui.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.businessLogic.adapter.HomeAdapter
import com.santhi.collegenetwork.businessLogic.viewModel.PostViewModel
import com.santhi.collegenetwork.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private lateinit var viewModel:PostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[PostViewModel::class.java]
        binding =  FragmentHomeBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        binding.postRv.layoutManager = LinearLayoutManager(requireContext())
        val adapter = HomeAdapter(requireContext())
        viewModel.dataList.observe(requireActivity()){newList->
            adapter.updateList(newList)
        }
        binding.postRv.adapter = adapter

        return binding.root
    }

}