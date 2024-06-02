package com.santhi.collegenetwork.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.businessLogic.adapter.MessageProfileAdapter
import com.santhi.collegenetwork.businessLogic.viewModel.ChatHistoryViewModel
import com.santhi.collegenetwork.databinding.ActivityMessageBinding

class MessageActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMessageBinding
    private lateinit var viewModel: ChatHistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMessageBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[ChatHistoryViewModel::class.java]
        val adapter = MessageProfileAdapter(this)
        binding.messageRv.layoutManager = LinearLayoutManager(this)
        viewModel.dataList.observe(this){newList->
            adapter.update(newList)
            // Toast.makeText(requireContext(), "$newList", Toast.LENGTH_SHORT).show()
        }
        binding.back.setOnClickListener {
            finish()
        }
        binding.messageRv.adapter = adapter
        setContentView(binding.root)
    }
}