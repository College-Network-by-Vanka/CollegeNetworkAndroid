package com.santhi.collegenetwork.ui.notificationSee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.santhi.collegenetwork.MainActivity.Companion.getNotiList
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.businessLogic.adapter.NotificationAdapter
import com.santhi.collegenetwork.businessLogic.model.NotificationModel
import com.santhi.collegenetwork.businessLogic.viewModel.NotificationViewModel
import com.santhi.collegenetwork.databinding.ActivityNotificarionBinding

class NotificarionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificarionBinding
    private lateinit var viewModel: NotificationViewModel
    private lateinit var adapter: NotificationAdapter
    private lateinit var list: MutableList<NotificationModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificarionBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[NotificationViewModel::class.java]
        setContentView(binding.root)


        list = mutableListOf()
        list.addAll(getNotiList())
        adapter = NotificationAdapter(this, list)
        adapter.notifyDataSetChanged()


        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.back.setOnClickListener {
            finish()
        }
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true // Reverse the layout
        layoutManager.stackFromEnd = true // Display items from the end
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        //adapter.updateData( list)
//        viewModel.liveData.observe(viewLifecycleOwner){newData->
//
//            adapter.updateData(newData)
//
//        }


//        viewModel.isLoading.observe(requireActivity()){isLoading->
//            if (!isLoading){
        //binding.shimmer.hideShimmer()
//        binding.recyclerView.visibility = View.VISIBLE
//        binding.shimmer.visibility = View.GONE
        //  }
//
//        }
//        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // adapter.list.clear()
        //  adapter.updateData(list)
    }
}