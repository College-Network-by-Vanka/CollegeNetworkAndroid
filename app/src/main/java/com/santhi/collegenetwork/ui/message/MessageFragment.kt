package com.santhi.collegenetwork.ui.message

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.businessLogic.adapter.MessageProfileAdapter
import com.santhi.collegenetwork.businessLogic.model.ChatHistoryModel
import com.santhi.collegenetwork.businessLogic.viewModel.ChatHistoryViewModel
import com.santhi.collegenetwork.databinding.FragmentMessageBinding


class MessageFragment : Fragment() {
    private lateinit var binding:FragmentMessageBinding
    private lateinit var viewModel:ChatHistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentMessageBinding.inflate(LayoutInflater.from(context),container,false)
       viewModel = ViewModelProvider(requireActivity())[ChatHistoryViewModel::class.java]
        val adapter = MessageProfileAdapter(requireContext())
        binding.messageRv.layoutManager = LinearLayoutManager(requireContext())
        viewModel.dataList.observe(requireActivity()){newList->
            adapter.update(newList)
           // Toast.makeText(requireContext(), "$newList", Toast.LENGTH_SHORT).show()
            if (newList.isEmpty()) {
                binding.noChatsMessage.visibility = View.VISIBLE
                binding.messageRv.visibility = View.GONE
            } else {
                binding.noChatsMessage.visibility = View.GONE
                binding.messageRv.visibility = View.VISIBLE
            }
        }
        binding.messageRv.adapter = adapter

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        messageCountZero()
    }
    private fun messageCountZero(){
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val database =
            Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/")

        //Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show()
        database.reference.child("chatsAlerts").child(uid).setValue(0)

    }


}