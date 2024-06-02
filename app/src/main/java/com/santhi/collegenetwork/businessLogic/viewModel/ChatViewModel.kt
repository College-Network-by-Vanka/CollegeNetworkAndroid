package com.santhi.collegenetwork.businessLogic.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.santhi.collegenetwork.businessLogic.model.ChatModel
import com.santhi.collegenetwork.businessLogic.model.PostModel

class ChatViewModel:ViewModel() {
    private val database = Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("chats")
    val dataList = MutableLiveData<List<ChatModel>>()
    val isLoading = MutableLiveData<Boolean>()

    fun fetchDataFromDatabase(roomId:String) {
        isLoading.value = true

        database.child(roomId).orderByChild("sendMessageTime").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = mutableListOf<ChatModel>()
                for (childSnapshot in snapshot.children) {
                    val yourData = childSnapshot.getValue(ChatModel::class.java)
                    yourData?.let { data.add(it) }
                    //Toast.makeText(context, yourData.toString(), Toast.LENGTH_SHORT).show()
                }
                dataList.value = data
                isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}