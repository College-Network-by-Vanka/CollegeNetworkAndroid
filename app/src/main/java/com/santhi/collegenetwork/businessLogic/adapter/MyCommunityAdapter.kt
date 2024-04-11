package com.santhi.collegenetwork.businessLogic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.santhi.collegenetwork.businessLogic.model.CommunityModel
import com.santhi.collegenetwork.databinding.MyCommunityCardBinding

class MyCommunityAdapter(private val context: Context):RecyclerView.Adapter<MyCommunityAdapter.ViewHolder>() {
    private var list:List<CommunityModel> = emptyList()
    fun addNew(newList:ArrayList<CommunityModel>){
        list = newList
    }
    inner class ViewHolder(val  binding:MyCommunityCardBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MyCommunityCardBinding.inflate(LayoutInflater.from(context),parent,false))
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