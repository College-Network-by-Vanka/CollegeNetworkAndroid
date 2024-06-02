package com.santhi.collegenetwork.businessLogic.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.santhi.collegenetwork.businessLogic.model.CommunityModel
import com.santhi.collegenetwork.businessLogic.repository.CommunityRepository
import com.santhi.collegenetwork.databinding.CommunityCardBinding
import com.santhi.collegenetwork.ui.community.CommunityDetailsActivity

class CommunityAdapter(val context: Context):RecyclerView.Adapter<CommunityAdapter.ViewHolder>() {
    private var  list = emptyList<CommunityModel>()

    fun addNewList(newlist: List<CommunityModel>){
        list = newlist
        notifyDataSetChanged()
    }
    inner class ViewHolder(val binding:CommunityCardBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         return ViewHolder(CommunityCardBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val communityRepo = CommunityRepository(context)
        holder.binding.appName.text = list[position].name
        Glide.with(context).load(list[position].logo).into(holder.binding.appIcon)
        holder.binding.appDescription.text = list[position].members.toString()+" "+"members"
        holder.binding.root.setOnClickListener {
            val intent = Intent(context,CommunityDetailsActivity::class.java)
            intent.putExtra("id",list[position].id)
            intent.putExtra("name",list[position].name)
            intent.putExtra("logo",list[position].logo)
            intent.putExtra("members",list[position].members.toString())
            intent.putExtra("description",list[position].description)
            context.startActivity(intent)
        }



    }
}