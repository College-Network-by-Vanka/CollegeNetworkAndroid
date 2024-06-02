 package com.santhi.collegenetwork

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.santhi.collegenetwork.businessLogic.localStorage.LocalStorageClass
import com.santhi.collegenetwork.businessLogic.model.NotificationModel
import com.santhi.collegenetwork.businessLogic.viewModel.NotificationViewModel
import com.santhi.collegenetwork.databinding.ActivityMainBinding

import com.santhi.collegenetwork.ui.community.CommunityFragment
import com.santhi.collegenetwork.ui.home.fragment.HomeFragment
import com.santhi.collegenetwork.ui.message.MessageFragment
import com.santhi.collegenetwork.ui.notificationSee.NotificarionActivity
import com.santhi.collegenetwork.ui.postUpload.UploadPostActivity
import com.santhi.collegenetwork.ui.postUpload.UploadPostBottomSheetFragment
import com.santhi.collegenetwork.ui.search.SearchActivity
import com.santhi.collegenetwork.ui.settings.SettingsActivity
import okio.blackholeSink

 class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
     private lateinit var viewModel: NotificationViewModel

     companion object {
         @JvmStatic
         private var notiList: MutableList<NotificationModel> = mutableListOf()

         fun getNotiList(): MutableList<NotificationModel> {
             return notiList
         }
         fun updateNotiList(newList: MutableList<NotificationModel>) {
             notiList.clear()
             notiList.addAll(newList)
         }
     }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        notiList = mutableListOf()

        viewModel = ViewModelProvider(this)[NotificationViewModel::class.java]
        supportActionBar?.hide()
        viewModel.liveData.observe(this){newData->
            updateNotiList(newData)


        }
//        binding.profile.setOnClickListener{
//            binding.titleText.text= "Settings"
//            intentFragment(R.id.frag,SettingsFragment(),this,"SettingFragment")
//        }
        viewModel.countValue.observe(this){notificationCount->
            if (notificationCount>0){
                binding.notificationBadge.visibility = View.VISIBLE
                binding.notificationBadge.text = notificationCount.toString()
            }
            else{
                binding.notificationBadge.visibility = View.INVISIBLE
            }

        }

        binding.notificationBtn.setOnClickListener {
            startActivity(Intent(this,NotificarionActivity::class.java))
        }
        binding.titleText.text = "Home"
        val localStorageClass = LocalStorageClass(this)
        val profile = localStorageClass.getString("profile")
        val bottomNav = binding.bottomNavigationView
      //  Glide.with(this).load(profile).listener(object :Re).into(binding.profile)
        Glide.with(this)
            .load(profile)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    Toast.makeText(this@MainActivity, "Failed", Toast.LENGTH_SHORT).show()
                    binding.shemmer.hideShimmer()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.shemmer.hideShimmer()
                    return false
                }


            })
            .into(binding.profile)
        loadFragment(
            HomeFragment(),
            true
        )
        binding.profile.setOnClickListener {
            startActivity(Intent(this,SettingsActivity::class.java))
        }
        binding.search.setOnClickListener {
            startActivity(Intent(this,SearchActivity::class.java))
        }

        bottomNav.setOnItemSelectedListener { menu->
            when(menu.itemId){

                R.id.home->{
                    binding.titleText.text = "Home"
                    loadFragment(
                        HomeFragment(),
                        true
                    )
                    true
                }
                R.id.community->{
                    binding.titleText.text = "Community"
                    loadFragment(
                    CommunityFragment(),
                    true
                    )
                    true
                }
                R.id.add->{

                    startActivity(Intent(this,UploadPostActivity::class.java))
                    true
                }
                R.id.chats->{
                    binding.titleText.text = "Chats"
                    loadFragment(
                        MessageFragment()
                    )
                    true
                }


                else->true

            }

        }
    }

     fun loadFragment(
         fragment: Fragment,
         addToBackStack: Boolean = false
     ) {
         val transaction = supportFragmentManager.beginTransaction()
         transaction.replace(R.id.frag, fragment)

         if (addToBackStack) {
             transaction.addToBackStack(null)
         }

         transaction.commit()
     }
     private fun messageCount(){
         val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
         val database =
             Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/")

         //Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show()
         database.reference.child("chatsAlerts").child(uid).addValueEventListener(object :ValueEventListener{
             override fun onDataChange(snapshot: DataSnapshot) {
                 if (snapshot.exists()) {
                     val count = snapshot.value.toString()
                     binding.bottomNavigationView.getOrCreateBadge(R.id.chats).apply {
                         number = count.toInt()
                         isVisible = number != 0
                     }
                 }
             }

             override fun onCancelled(error: DatabaseError) {
             }

         })

     }


     override fun onStart() {
         super.onStart()
         messageCount()
         val uid = FirebaseAuth.getInstance().currentUser?.uid
         Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("users").child(uid.toString())
             .child("online").setValue("online")
     }

     override fun onDestroy() {
         super.onDestroy()
         val uid = FirebaseAuth.getInstance().currentUser?.uid
         Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("users").child(uid.toString())
             .child("online").setValue("offline")
     }
     @Deprecated("Deprecated in Java")
     override fun onBackPressed() {
         // Create an AlertDialog to confirm if the user wants to exit
         AlertDialog.Builder(this).apply {
             setTitle("Exit")
             setMessage("Are you sure you want to go back?")
             setPositiveButton("Yes") { dialog, _ ->
                 dialog.dismiss()
                 finishAffinity()
                 super.onBackPressed() // Call the super method to handle the back press
             }
             setNegativeButton("No") { dialog, _ ->
                 dialog.dismiss() // Just dismiss the dialog
             }
             create()
             show()
         }
     }

 }