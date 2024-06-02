package com.santhi.collegenetwork.businessLogic.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.santhi.collegenetwork.businessLogic.model.Clubkey
import com.santhi.collegenetwork.businessLogic.model.CommunityModel
import com.santhi.collegenetwork.databinding.MyCommunityCardBinding
import com.santhi.collegenetwork.ui.community.CommunityDetailsActivity

class MyCommunityAdapter(private val context: Context):RecyclerView.Adapter<MyCommunityAdapter.ViewHolder>() {
    private var list:List<CommunityModel> = emptyList()
    private var clubKeyList:List<Clubkey> = emptyList()
    private  var filteredList:MutableList<CommunityModel> = mutableListOf<CommunityModel>()
    fun addNew(newList:List<CommunityModel>){
        list = newList

    }
    fun getMyList(newList: List<Clubkey>){

        clubKeyList = newList


        for (clubKey in clubKeyList) {
            val filteredItems = list.filter { it.id == clubKey.clubkey }
            filteredList.addAll(filteredItems)
            Toast.makeText(context, "$filteredList", Toast.LENGTH_SHORT).show()
        }

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
        //Toast.makeText(context, "$filteredList+jj", Toast.LENGTH_SHORT).show()
        Glide.with(context).load(list[position].logo).into(holder.binding.ivCommunityIcon)
        holder.binding.root.setOnClickListener {
            val intent = Intent(context, CommunityDetailsActivity::class.java)
            intent.putExtra("id",list[position].id)
            intent.putExtra("name",list[position].name)
            intent.putExtra("logo",list[position].logo)
            intent.putExtra("members",list[position].members.toString())
            intent.putExtra("description",list[position].description)
            context.startActivity(intent)
        }
        holder.binding.tvCommunityMembers.text =list[position].members.toString()+" "+"members"

    }
}