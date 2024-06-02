package com.santhi.collegenetwork.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.santhi.collegenetwork.businessLogic.adapter.ChatAdapter
import com.santhi.collegenetwork.businessLogic.localStorage.LocalStorageClass
import com.santhi.collegenetwork.businessLogic.model.ChatModel
import com.santhi.collegenetwork.businessLogic.repository.ChatRepository

import com.santhi.collegenetwork.businessLogic.viewModel.ChatViewModel
import com.santhi.collegenetwork.databinding.ActivityChatBinding
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class ChatActivity : AppCompatActivity() {
    private lateinit var binding:ActivityChatBinding
    private lateinit var viewModel: ChatViewModel
    private var unSeen:Int =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        setContentView(binding.root)
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        val chatRepository = ChatRepository(this)
        val reciverId = intent.getStringExtra("id")
        val reciver_token =  intent.getStringExtra("token")
        val senderId = FirebaseAuth.getInstance().currentUser?.uid
        val room = senderId+reciverId
        viewModel.fetchDataFromDatabase(room)
        val reciverName = intent.getStringExtra("name")
        binding.username.text = reciverName
        val reciverProfile = intent.getStringExtra("img")
       // update(reciverId.toString())
        updateSeen(senderId.toString(),reciverId.toString())
         val adapter = ChatAdapter(this,reciverName.toString(),reciverProfile.toString())

        binding.sendButton.setOnClickListener {
            val currentTime: LocalTime = LocalTime.now()

            // Define the desired time format
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a")


            // Format the current time using the formatter
            val formattedTime: String = currentTime.format(formatter)
            val localStorageClass  = LocalStorageClass(this)
            val myname =localStorageClass.getString("userName")
            val text = binding.messageInput.text.toString()
            if (text.isNotEmpty()){
                val chatModel = ChatModel(text,formattedTime,senderId,reciverId,false, UUID.randomUUID().toString())
                chatRepository.sendMessage(chatModel,reciver_token.toString(),unSeen,myname)

                binding.messageInput.setText("")
            }


        }
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        viewModel.dataList.observe(this){list->
            adapter.update(list)
            binding.chatRecyclerView.scrollToPosition(list.size-1)
            for( i in list){
                if (!i.seen&&i.senderId==uid){
                    unSeen++
                }
            }


        }
        binding.chatRecyclerView.adapter = adapter

          status(reciverId.toString())

    }
    private fun updateSeen(senderId: String,reciverId:String){
        val room = senderId+reciverId
        Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("chatHistory")
            .child(senderId).child(room).child("unseen").setValue(0)


    }
    private  fun status(reciverId: String)  {
        Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .reference.child("users").child(reciverId.toString())
            .child("online").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val status = snapshot.value.toString()
                    binding.onlineStatus.text = status
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle any errors here
                }
            })
    }












}