package com.santhi.collegenetwork.businessLogic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.santhi.collegenetwork.businessLogic.model.ChatModel
import com.santhi.collegenetwork.databinding.MessageBinding

class ChatAdapter(
    private val context: Context,
    private val receiverName: String,
    private val receiverProfile: String
) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private var list: List<ChatModel> = emptyList()

    fun update(newList: List<ChatModel>) {
        list = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: MessageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MessageBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (list[position].senderId == uid) {
            holder.binding.sentMessageContainer.visibility = View.VISIBLE
            holder.binding.sentMessage.visibility = View.VISIBLE
            holder.binding.status.visibility = View.VISIBLE
            holder.binding.senderTimestamp.visibility = View.VISIBLE

            holder.binding.sentMessage.text = list[position].message
            holder.binding.senderTimestamp.text = list[position].sendMessageTime

            if (list[position].seen) {
                holder.binding.status.text = "seen"
            } else {
                holder.binding.status.text = "sent"
            }

            // Hide views that should not be visible for received messages
            holder.binding.avatar.visibility = View.GONE
            holder.binding.receivedMessageContainer.visibility = View.GONE
            holder.binding.receivedMessage.visibility = View.GONE 
            holder.binding.receivedName.visibility = View.GONE
            holder.binding.reciverTimestamp.visibility = View.GONE

        } else {
            val roomId = list[position].senderId+uid

            seenMessageUpdate(list[position].id.toString(),roomId)
            holder.binding.avatar.visibility = View.VISIBLE
            holder.binding.receivedMessageContainer.visibility = View.VISIBLE
            holder.binding.receivedMessage.visibility = View.VISIBLE
            holder.binding.receivedName.visibility = View.VISIBLE
            holder.binding.reciverTimestamp.visibility = View.VISIBLE

            holder.binding.receivedMessage.text = list[position].message
            holder.binding.receivedName.text = receiverName
            Glide.with(context).load(receiverProfile).into(holder.binding.avatar)
            holder.binding.reciverTimestamp.text = list[position].sendMessageTime

            // Hide views that should not be visible for sent messages
            holder.binding.sentMessageContainer.visibility = View.GONE
            holder.binding.sentMessage.visibility = View.GONE
            holder.binding.status.visibility = View.GONE
            holder.binding.senderTimestamp.visibility = View.GONE
        }
    }
    private fun seenMessageUpdate(id:String,roomId:String){
        Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("chats").child(roomId).child(id).child("seen").setValue(true)
    }
}
