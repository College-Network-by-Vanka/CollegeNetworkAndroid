package com.santhi.collegenetwork.businessLogic.repository

import android.content.Context
import android.widget.Toast
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.santhi.collegenetwork.businessLogic.model.ChatHistoryModel
import com.santhi.collegenetwork.businessLogic.model.ChatModel

class ChatRepository(val context: Context) {
    val DATABASE = Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("chats")

    fun sendMessage(chatData: ChatModel, token: String, unseen: Int, reciverName: String?){
        val notificationRepository = NotificationRepository()
       val senderRoom = chatData.senderId+chatData.reciverId
        val receiverRoom = chatData.reciverId+chatData.senderId
        DATABASE.child(senderRoom).child(chatData.id.toString()).setValue(chatData).addOnCompleteListener {
            if (it.isSuccessful){
                DATABASE.child(receiverRoom).child(chatData.id.toString()).setValue(chatData).addOnCompleteListener {
                    if (it.isSuccessful){
//                        getReciverUid(chatData.reciverId.toString()){isTrue->
//                            if(!isTrue){
                                notificationRepository.sendNotificationToFirebase(chatData.senderId.toString(),chatData.reciverId.toString(),"$reciverName sends new message",chatData.message.toString(),token,"Chat",context,true)
                                sendChatHistory(chatData.senderId.toString(),chatData.reciverId.toString(),chatData.message.toString(),chatData.sendMessageTime.toString(),unseen)

                         //   }
                    //    }
                    }
                else{

                    }
                }
            }else{
                Toast.makeText(context, "error network", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun sendChatHistory(senderId:String,receiverId:String,message:String,time:String,unseen:Int){
        val senderRoom = senderId+receiverId
        val receiverRoom = receiverId+senderId
        val senderChatHistoryModel = ChatHistoryModel(senderId,message,time,unseen+1,"not you")
        val receiverChatHistoryModel = ChatHistoryModel(receiverId,message,time,0,"you")
        Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("chatHistory").child(receiverId).child(receiverRoom).setValue(senderChatHistoryModel).addOnCompleteListener {
            if (it.isSuccessful){
                Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("chatHistory").child(senderId).child(senderRoom).setValue(receiverChatHistoryModel)
            }else{
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
            }
        }
    }


}