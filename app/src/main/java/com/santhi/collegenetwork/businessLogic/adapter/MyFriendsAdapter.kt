package com.santhi.collegenetwork.businessLogic.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.santhi.collegenetwork.businessLogic.localStorage.LocalStorageClass
import com.santhi.collegenetwork.databinding.UserCardBinding
import com.santhi.collegenetwork.ui.profile.ProfileActivity
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MyFriendsAdapter(private val context: Context,private val list:List<String>):RecyclerView.Adapter<MyFriendsAdapter.ViewHolder>() {
   data class  UserDataClass(
       var name:String,
       var profile:String,
       var token:String
   )
    inner class ViewHolder(val binding:UserCardBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
           return ViewHolder(UserCardBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        CoroutineScope(Dispatchers.Main).launch {
            val (name,profile,token) = getUserName(list[position].toString())
            holder.binding.userName.text=  name
            loadImageWithProgressBar(profile,holder.binding.profile)
            holder.binding.root.setOnClickListener {
                val intent = Intent(context, ProfileActivity::class.java)
                intent.putExtra("userId", list[position])
                intent.putExtra("token", token)
                intent.putExtra("profile", profile)
                intent.putExtra("name", name)


                context.startActivity(intent)
            }

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
    suspend fun getUserName(uid: String): UserDataClass= withContext(Dispatchers.IO) {
        val localStorage = LocalStorageClass(context)


        val database = Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .reference
            .child("users")
            .child(uid)

        val img = getProfile(database)
        val name = getName(database)
        val token =  getToken(database)

        UserDataClass(name, img,token)
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