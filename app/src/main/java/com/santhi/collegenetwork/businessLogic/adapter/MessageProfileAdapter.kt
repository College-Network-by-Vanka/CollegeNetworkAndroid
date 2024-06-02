package com.santhi.collegenetwork.businessLogic.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.santhi.collegenetwork.businessLogic.localStorage.LocalStorageClass
import com.santhi.collegenetwork.businessLogic.model.ChatHistoryModel
import com.santhi.collegenetwork.databinding.SenderLayoutBinding
import com.santhi.collegenetwork.ui.chat.ChatActivity
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MessageProfileAdapter(private val  context: Context):Adapter<MessageProfileAdapter.ViewHolder>() {
    var list = emptyList<ChatHistoryModel>()
    var name = ""
    var img = ""
    var get_token = ""
    fun update(newList:List<ChatHistoryModel>){
        list = newList
        notifyDataSetChanged()
    }
    data class UserProfile(
        val name: String,
        val profileImageUrl: String,
        val college: String
    )

    inner class ViewHolder(val binding:SenderLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(SenderLayoutBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.unreadCount.visibility = View.VISIBLE
        if (list[position].unseen==0){
            holder.binding.unreadCount.visibility = View.GONE
        }
        CoroutineScope(Dispatchers.Main).launch {

            val (Name, Img,token) =  getUserName(list[position].id.toString())
            name = Name
            img = Img
            get_token = token
            holder.binding.senderName.text = Name

            loadImageWithProgressBar(Img,holder.binding.profileImage)
            holder.binding.root.setOnClickListener {
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("id",list[position].id)
                intent.putExtra("name",Name)
                intent.putExtra("img",Img)
                intent.putExtra("token",token)
                context.startActivity(intent)
            }

        }



        if (list[position].type=="you"){
            holder.binding.messageText.text = "you"
            holder.binding.timestamp.text = list[position].recentMessage

            holder.binding.unreadCount.text = list[position].unseen.toString()

        }else{
            holder.binding.messageText.text = list[position].recentMessage
            holder.binding.timestamp.text = list[position].time
            holder.binding.unreadCount.text = list[position].unseen.toString()
        }


    }
    suspend fun getProfile(database: DatabaseReference): String =  withContext(Dispatchers.IO) {
        database.child("profile").get().await().value.toString()
    }

    suspend fun getName(database: DatabaseReference): String = withContext(Dispatchers.IO) {
        database.child("name").get().await().value.toString()
    }
    suspend fun getToken(database: DatabaseReference): String = withContext(Dispatchers.IO) {

        database.child("notification").get().await().value.toString()
    }

    suspend fun getUserName(uid: String): UserProfile = withContext(Dispatchers.IO) {
        val localStorage = LocalStorageClass(context)
        val college =localStorage.getString("college")

        val database = Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .reference
            .child("users")
            .child(uid)

        val profile = getProfile(database)
        val name = getName(database)
        val getToken = getToken(database)

        UserProfile(name, profile, getToken)
    }
    fun loadImageWithProgressBar(urlImage: String, profileImage: CircleImageView){
        // Show progress bar

        // binding.shemmer.startShimmer()
        Glide.with(context)
            .load(urlImage)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    //  binding.shemmer.hideShimmer()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    // binding.shemmer.hideShimmer()
                    //binding.ivUserProfile.background = ColorDrawable(Color.WHITE)
                    return false
                }

            })
            .into(profileImage)

    }

}