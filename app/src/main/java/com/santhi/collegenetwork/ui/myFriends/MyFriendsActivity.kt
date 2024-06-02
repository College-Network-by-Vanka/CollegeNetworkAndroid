package com.santhi.collegenetwork.ui.myFriends

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.businessLogic.adapter.MyFriendsAdapter
import com.santhi.collegenetwork.businessLogic.viewModel.MyFriendsViewModel
import com.santhi.collegenetwork.databinding.ActivityMyFriendsBinding

class MyFriendsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMyFriendsBinding
    private lateinit var viewModel:MyFriendsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyFriendsBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[MyFriendsViewModel::class.java]
        setContentView(binding.root)
        binding.friendRv.layoutManager =LinearLayoutManager(this)
        binding.back.setOnClickListener {
            finish()
        }
        viewModel.dataList.observe(this){
            if (it.isEmpty()) {
                binding.noChatsMessage.visibility = View.VISIBLE
                binding.friendRv.visibility = View.GONE
            } else {
                binding.noChatsMessage.visibility = View.GONE
                binding.friendRv.visibility = View.VISIBLE
            }
            binding.friendRv.adapter = MyFriendsAdapter(this@MyFriendsActivity,it)

        }
    }
}