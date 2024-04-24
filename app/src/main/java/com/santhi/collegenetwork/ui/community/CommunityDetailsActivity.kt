package com.santhi.collegenetwork.ui.community

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.businessLogic.Repository.CommunityRepository
import com.santhi.collegenetwork.databinding.ActivityCommunityDetailsBinding

class CommunityDetailsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCommunityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val communityId = intent.getStringExtra("id")
        val communityName = intent.getStringExtra("name")
        val communityLogo = intent.getStringExtra("logo")
        val communityMembers = intent.getStringExtra("members")
        val communityDescription = intent.getStringExtra("description")
        val communityRepo = CommunityRepository(this)
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

    }
}