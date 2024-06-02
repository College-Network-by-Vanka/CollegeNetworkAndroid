package com.santhi.collegenetwork.businessLogic.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.santhi.collegenetwork.businessLogic.model.ChatHistoryModel
import com.santhi.collegenetwork.businessLogic.model.ChatModel

class ChatHistoryViewModel:ViewModel() {
    private val database = Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("chatHistory")
    val dataList = MutableLiveData<List<ChatHistoryModel>>()
    val isLoading = MutableLiveData<Boolean>()
   init {
       fetchDataFromDatabase()
   }
    fun fetchDataFromDatabase() {
        isLoading.value = true
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        database.child(uid.toString()).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = mutableListOf<ChatHistoryModel>()
                for (childSnapshot in snapshot.children) {
                    val yourData = childSnapshot.getValue(ChatHistoryModel::class.java)
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