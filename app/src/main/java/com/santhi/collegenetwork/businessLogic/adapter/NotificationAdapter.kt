package com.santhi.collegenetwork.businessLogic.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.santhi.collegenetwork.MainActivity
import com.santhi.collegenetwork.businessLogic.repository.NotificationRepository
import com.santhi.collegenetwork.businessLogic.model.NotificationModel
import com.santhi.collegenetwork.databinding.NotificationItemBinding
import com.santhi.collegenetwork.ui.MessageActivity
import com.santhi.collegenetwork.ui.home.activity.SeeReplyActivity
import com.santhi.collegenetwork.ui.myFriends.MyFriendsActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationAdapter(val context: Context, val list:MutableList<NotificationModel>):
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: NotificationItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(NotificationItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val notificationRepository = NotificationRepository()




        val grey = Color.GRAY
        val  white = Color.WHITE

        holder.binding.notificationText.text =list[position].title
        holder.binding.timeText.text = getRemainingTime(list[position].time.toString()).toString()
        if (   list[position].seen){
            holder.binding.notificationCard.setCardBackgroundColor(grey)
        }else{
            holder.binding.notificationCard.setCardBackgroundColor(white)
        }
        holder.binding.notificationCard.setOnClickListener {
            notificationRepository.seen(uid.toString(),list[position].notificationId.toString())

            //notifyItemRangeChanged(0,position)
            notifyDataSetChanged()
            if (list[position].to == "Chat"){
                val intent = Intent(context,MessageActivity::class.java)
                context.startActivity(intent)
            }else if(list[position].to=="friend") {
                val intent = Intent(context,MyFriendsActivity::class.java)
                context.startActivity(intent)

            }else
             {

                val intent = Intent(context, SeeReplyActivity::class.java)
                intent.putExtra("path", list[position].to)
                intent.putExtra("isNotification", true)
                intent.putExtra("userPath", list[position].from)
                context.startActivity(intent)
            }

        }






    }
    fun getRemainingTime(dateString: String): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val date = dateFormat.parse(dateString)
        val currentTime = Date()

        val differenceInMillis = currentTime.time - date.time
        val seconds = differenceInMillis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            days > 0 -> "$days days"
            hours > 0 -> "$hours hrs"
            minutes > 0 -> "$minutes min"
            else -> "$seconds sec"
        }
    }

}