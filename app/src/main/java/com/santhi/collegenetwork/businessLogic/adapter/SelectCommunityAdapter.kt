package com.santhi.collegenetwork.businessLogic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.santhi.collegenetwork.businessLogic.repository.CommunityRepository
import com.santhi.collegenetwork.businessLogic.localStorage.LocalStorageClass
import com.santhi.collegenetwork.businessLogic.model.CommunityModel
import com.santhi.collegenetwork.businessLogic.model.PostModel
import com.santhi.collegenetwork.businessLogic.tokenGenrator.TokenManager
import com.santhi.collegenetwork.databinding.MyCommunityCardBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class SelectCommunityAdapter(private val  context: Context,private val postText:String,private val postImg:String,private val isAnonymously:Boolean):RecyclerView.Adapter<SelectCommunityAdapter.ViewHolder> (){
    var list = emptyList<CommunityModel>()
    fun updateList(newList: List<CommunityModel>){
        list = newList
        notifyDataSetChanged()
    }
    inner class ViewHolder(val  binding: MyCommunityCardBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MyCommunityCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
      return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvCommunityName.text = list[position].name
        //Toast.makeText(context, "$filteredList+jj", Toast.LENGTH_SHORT).show()
        Glide.with(context).load(list[position].logo).into(holder.binding.ivCommunityIcon)
        holder.binding.tvCommunityMembers.text =list[position].members.toString()+" "+"members"

         val localStorageClass = LocalStorageClass(context)
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val communityRepository = CommunityRepository(context)
        holder.binding.root.setOnClickListener {
            val tokenManager = TokenManager(context)

          val noti =   tokenManager.getSavedToken()
            val postUid = UUID.randomUUID().toString()
            val getCurrentTime = getCurrentDateTime()
            val path = "post/${postUid}"
            val  post = PostModel(postText,postImg,0,0,getCurrentTime,postUid,uid,noti,path,list[position].name,isAnonymously)

         communityRepository.uploadPost(context,post,list[position].id.toString())

        }
    }
    fun getCurrentDateTime(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return currentDateTime.format(formatter)
    }
}