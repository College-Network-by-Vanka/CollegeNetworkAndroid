package com.santhi.collegenetwork.businessLogic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.santhi.collegenetwork.businessLogic.model.CommunityModel
import com.santhi.collegenetwork.databinding.CommunityCardBinding

class CommunityAdapter(val context: Context):RecyclerView.Adapter<CommunityAdapter.ViewHolder>() {
    private var  list = emptyList<CommunityModel>()
    fun addNewList(newlist: ArrayList<CommunityModel>){
        list = newlist
    }
    inner class ViewHolder(val binding:CommunityCardBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         return ViewHolder(CommunityCardBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvCommunityName.text = list[position].name
        Glide.with(context).load(list[position].logo).into(holder.binding.ivCommunityIcon)
        holder.binding.tvCommunityMembers.text = list[position].members+" "+"members"

    }
}