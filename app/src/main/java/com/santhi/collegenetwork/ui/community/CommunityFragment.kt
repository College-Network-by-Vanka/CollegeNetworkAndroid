package com.santhi.collegenetwork.ui.community

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.businessLogic.adapter.CommunityAdapter
import com.santhi.collegenetwork.businessLogic.adapter.MyCommunityAdapter
import com.santhi.collegenetwork.businessLogic.dummy.Dummy
import com.santhi.collegenetwork.databinding.FragmentCommunityBinding


class CommunityFragment : Fragment() {
    private lateinit var binding:FragmentCommunityBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommunityBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        binding.rvCommunities.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        val dummmy = Dummy()
       val comAd = CommunityAdapter(requireContext())
        comAd.addNewList(dummmy.dummyList())
        binding.rvCommunities.adapter =comAd
        binding.mycomRv.layoutManager = LinearLayoutManager(requireContext())
        val myComAd = MyCommunityAdapter(requireContext())
        myComAd.addNew(dummmy.dummyList())
        binding.mycomRv.adapter = myComAd



        return binding.root
    }


}