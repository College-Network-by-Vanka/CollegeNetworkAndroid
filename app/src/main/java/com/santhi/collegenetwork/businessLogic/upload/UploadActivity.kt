package com.santhi.collegenetwork.businessLogic.upload

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import com.santhi.collegenetwork.MainActivity
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.businessLogic.localStorage.LocalStorageClass
import com.santhi.collegenetwork.businessLogic.model.User
import com.santhi.collegenetwork.businessLogic.repository.CommunityRepository
import com.santhi.collegenetwork.businessLogic.tokenGenrator.TokenManager
import com.santhi.collegenetwork.databinding.ActivityUploadBinding
import com.santhi.collegenetwork.ui.auth.PermissionActivity

class UploadActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUploadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val tokenManager = TokenManager(this)

        tokenManager.getSavedToken()

        val notificationID = tokenManager.getSavedToken()
        val localStorage = LocalStorageClass(this)
        val email = localStorage.getString("email")
        localStorage.saveString("notification",notificationID.toString())
        val gender = localStorage.getString("gender")
        val college = localStorage.getString("college")
        val userName = localStorage.getString("userName")
        val year = localStorage.getString("year")
        val branch = localStorage.getString("branch")
        val section = localStorage.getString("sec")
        val profile = localStorage.getString("profile")
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val userModel = User(userName,gender,college,year,branch,section,uid,notificationID,profile)
        val roomId = year+branch+section
        Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("users").child(uid.toString()).setValue(userModel).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(this, "Sucess", Toast.LENGTH_SHORT).show()
                val communityRepository = CommunityRepository(this)
                communityRepository.joinCommunity("3")
                startActivity(Intent(this, PermissionActivity::class.java))
            }else{
                Toast.makeText(this, "Some thing error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}