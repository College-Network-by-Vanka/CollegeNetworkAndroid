package com.santhi.collegenetwork.ui.community

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.businessLogic.adapter.CommunityAdapter
import com.santhi.collegenetwork.businessLogic.adapter.MyCommunityAdapter
import com.santhi.collegenetwork.businessLogic.dummy.Dummy
import com.santhi.collegenetwork.businessLogic.model.CommunityModel
import com.santhi.collegenetwork.businessLogic.viewModel.CommunityViewModel
import com.santhi.collegenetwork.businessLogic.viewModel.MyCommunityViewModel
import com.santhi.collegenetwork.databinding.FragmentCommunityBinding


class CommunityFragment : Fragment() {
    private lateinit var binding:FragmentCommunityBinding
    private lateinit var viewModel:CommunityViewModel
   private lateinit var myCommunityViewModel: MyCommunityViewModel
    private var filterList: MutableList<CommunityModel> = mutableListOf() // Declare as MutableList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[CommunityViewModel::class.java]

        myCommunityViewModel =ViewModelProvider(requireActivity())[MyCommunityViewModel::class.java]
        binding = FragmentCommunityBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        binding.shimmer.startShimmer()
       val comAd = CommunityAdapter(requireContext())


        val myComAd = MyCommunityAdapter(requireContext())



        viewModel.dataList.observe(viewLifecycleOwner) { communities1 ->
            filterList.clear()
            filterList.addAll(communities1)




            // Observe myCommunityViewModel.dataList after the initial data is loaded
            myCommunityViewModel.dataList.observe(viewLifecycleOwner) { communities ->
                val necessaryItemIdsSet: Set<String> = communities
                    .mapNotNull { it.clubkey?.toIntOrNull()?.toString() }
                    .toSet()

                val filteredItems: List<CommunityModel> = filterList.filter { item ->
                    necessaryItemIdsSet.contains(item.id.toString())
                }


                myComAd.addNew(filteredItems)
                comAd.addNewList(communities1)
            }
        }
        binding.mycomRv.layoutManager = LinearLayoutManager(requireContext())
        binding.mycomRv.adapter = myComAd

        binding.rvCommunities.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.rvCommunities.adapter =comAd
        viewModel.isLoading.observe(requireActivity())
        {isLoading->
            if (!isLoading){
                binding.shimmer.hideShimmer()
                binding.shimmer.visibility = View.GONE
                binding.rvCommunities.visibility = View.VISIBLE
                binding.mycomRv.visibility = View.VISIBLE
                binding.textView7.visibility = View.VISIBLE
                binding.textView8.visibility = View.VISIBLE
            }

        }

        return binding.root
    }


}