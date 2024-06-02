package com.santhi.collegenetwork.ui.community

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.santhi.collegenetwork.businessLogic.repository.CommunityRepository
import com.santhi.collegenetwork.businessLogic.adapter.HomeAdapter
import com.santhi.collegenetwork.businessLogic.viewModel.CommunityPostViewModel
import com.santhi.collegenetwork.databinding.ActivityCommunityDetailsBinding

class CommunityDetailsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCommunityDetailsBinding
    private lateinit var viewModel: CommunityPostViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[CommunityPostViewModel::class.java]
        binding = ActivityCommunityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.back.setOnClickListener {
            finish()
        }
        binding.rv.layoutManager = LinearLayoutManager(this)
        val adapter = HomeAdapter(this)

        val communityId = intent.getStringExtra("id")

        val communityName = intent.getStringExtra("name")
        val communityLogo = intent.getStringExtra("logo")
        val communityMembers = intent.getStringExtra("members")
        val communityDescription = intent.getStringExtra("description")
        val communityRepo = CommunityRepository(this)
        viewModel.fetchDataFromDatabase(communityName.toString())
        communityRepo.checkMember(communityId.toString()){isMember->
              if (isMember){
                  binding.button.setBackgroundColor(Color.WHITE)
                  binding.button.text = "Joined"
                  binding.button.setTextColor(Color.BLACK)
              }else{
                  binding.button.setBackgroundColor(Color.parseColor("#F66A68"))
                  binding.button.text = "Join"
                  binding.button.setTextColor(Color.WHITE)
              }
        }
        binding.button.setOnClickListener {
            if (binding.button.text=="Joined"){
                communityRepo.removeCommunity(communityId.toString())
                binding.button.setBackgroundColor(Color.parseColor("#F66A68"))
                binding.button.text = "Join"
                binding.button.setTextColor(Color.WHITE)
            }else {
                communityRepo.joinCommunity(communityId.toString())
                binding.button.setBackgroundColor(Color.WHITE)
                binding.button.text = "Joined"
                binding.button.setTextColor(Color.BLACK)
            }


        }
        Glide.with(this).load(communityLogo).into(binding.ivCommunityIcon)
        binding.tvCommunityName.text = communityName
        binding.tvCommunityMembers.text = communityMembers
        binding.tvCommunityDescription.text = communityDescription
        viewModel.dataList.observe(this){
            adapter.updateList(it)
        }
        binding.rv.adapter = adapter
        viewModel.isLoading.observe(this){isLoading->
            if (!isLoading){
                binding.shimmer.hideShimmer()
                binding.shimmer.visibility = View.GONE
                binding.rv.visibility = View.VISIBLE
            }
        }

    }
}