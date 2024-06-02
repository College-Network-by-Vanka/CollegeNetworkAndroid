package com.santhi.collegenetwork.businessLogic.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.santhi.collegenetwork.businessLogic.model.User
import com.santhi.collegenetwork.databinding.UserCardBinding
import com.santhi.collegenetwork.ui.profile.ProfileActivity

class SearchAdapter(private val context: Context,private val userList:MutableList<User>):RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    inner class ViewHolder(val binding:UserCardBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder(UserCardBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return  userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       Glide.with(context).load(userList[position].profile).into( holder.binding.profile)
        holder.binding.userName.text = userList[position].name
        holder.binding.root.setOnClickListener {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra("userId", userList[position].userId)
            intent.putExtra("token",userList[position].notification.toString())
            intent.putExtra("profile",userList[position].profile)
            intent.putExtra("name",userList[position].name)


            context.startActivity(intent)
        }
    }
}