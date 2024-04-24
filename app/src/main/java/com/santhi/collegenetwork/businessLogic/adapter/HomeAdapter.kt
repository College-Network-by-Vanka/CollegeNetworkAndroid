package com.santhi.collegenetwork.businessLogic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.santhi.collegenetwork.businessLogic.model.PostModel
import com.santhi.collegenetwork.databinding.ImgPostBinding

class HomeAdapter(private val context: Context):RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    var list:List<PostModel> = emptyList()
    fun updateList(newList: List<PostModel>){
        list = newList
        notifyDataSetChanged()
    }
    inner class ViewHolder(val binding:ImgPostBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ImgPostBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
     if (list[position].postText.toString() == "noText"){
         holder.binding.tvPostDescription.visibility  = View.GONE
     }else{
         holder.binding.tvPostDescription.text = list[position].postText
     }
        if (list[position].postImg.toString() == "NoImg"){
            holder.binding.ivPostImage.visibility = View.GONE
        }else{
            Glide.with(context).load(list[position].postImg).into(holder.binding.ivPostImage)
        }
    }
}