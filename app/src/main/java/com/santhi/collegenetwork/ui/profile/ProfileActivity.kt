package com.santhi.collegenetwork.ui.profile

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.options
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.businessLogic.adapter.HomeAdapter
import com.santhi.collegenetwork.businessLogic.localStorage.LocalStorageClass
import com.santhi.collegenetwork.businessLogic.repository.NotificationRepository
import com.santhi.collegenetwork.businessLogic.viewModel.ProfilePostViewModel
import com.santhi.collegenetwork.databinding.ActivityProfileBinding
import com.santhi.collegenetwork.ui.chat.ChatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel:ProfilePostViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ProfilePostViewModel::class.java]
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userId = intent.getStringExtra("userId")
        val database = Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .reference
            .child("users")
            .child(userId.toString())
        viewModel.fetchDataFromDatabase(userId.toString())
        binding.back.setOnClickListener {
            finish()
        }
        val localStorageClass = LocalStorageClass(this)
        val myName = localStorageClass.getString("userName")
        val token = intent.getStringExtra("token")
        val name = intent.getStringExtra("name")
        val profile = intent.getStringExtra("profile")
        val adapter = HomeAdapter(this)
        checkIfFallow(userId.toString())
        binding.rv.layoutManager = LinearLayoutManager(this)
        viewModel.dataList.observe(this){list->
            adapter.updateList(list)
        }
        binding.btnMessage.setOnClickListener {
            val intent = Intent(this,ChatActivity::class.java)
            intent.putExtra("id",userId)
            intent.putExtra("name",name)
            intent.putExtra("img",profile)
            intent.putExtra("token",token)
            startActivity(intent)
        }
        binding.btnConnect.setOnClickListener {
            sendFallow(userId.toString(),uid.toString(),token.toString(),myName)
        }
        binding.rv.adapter = adapter
        CoroutineScope(Dispatchers.Main).launch {

            val college = getCollege(database)
            binding.userName.text = name
            binding.college.text = college
            loadImageWithProgressBar(profile.toString())
        }
    }
    
//    suspend fun getProfile(database: DatabaseReference): String =  withContext(Dispatchers.IO) {
//        database.child("profile").get().await().value.toString()
//    }

//    suspend fun getName(database: DatabaseReference): String = withContext(Dispatchers.IO) {
//        database.child("name").get().await().value.toString()
//    }
    suspend fun getCollege(database: DatabaseReference): String = withContext(Dispatchers.IO) {

        database.child("college").get().await().value.toString()
    }


    fun loadImageWithProgressBar(urlImage: String){
        // Show progress bar

        // binding.shemmer.startShimmer()
        Glide.with(this)
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
                    binding.ivUserProfile.background = ColorDrawable(Color.WHITE)
                    return false
                }

            })
            .into(binding.ivUserProfile)

    }
    private fun sendFallow(friendsUid:String,uid:String,token:String,name:String){
        val notificationRepository = NotificationRepository()

        Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("friends").child(uid.toString()).child(friendsUid).setValue(friendsUid).addOnCompleteListener {
            if (it.isSuccessful){
                binding.btnConnect.text = "Connected"
                binding.btnConnect.setBackgroundResource(R.drawable.connected_btn)
                notificationRepository.sendNotificationToFirebase(uid.toString(),friendsUid,"$name follows to you","new connection",token,"friend",this)


            }
        }
    }
    private fun checkIfFallow(friendsUid:String){
        val uid =FirebaseAuth.getInstance().currentUser?.uid

        Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("friends").child(uid.toString()).child(friendsUid).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    binding.btnConnect.text = "Connected"
                    binding.btnConnect.setBackgroundResource(R.drawable.connected_btn)
                    binding.btnConnect.textSize = 12f // Adjust the size as needed

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

}