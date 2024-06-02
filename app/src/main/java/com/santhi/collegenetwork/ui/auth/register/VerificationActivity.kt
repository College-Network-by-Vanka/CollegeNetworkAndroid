package com.santhi.collegenetwork.ui.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.businessLogic.upload.UploadActivity
import com.santhi.collegenetwork.databinding.ActivityVerificationBinding

class VerificationActivity : AppCompatActivity() {
    private lateinit var binding:ActivityVerificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.openMail.setOnClickListener {
            openGmail()
        }
        binding.verifyBtn.setOnClickListener {
            val auth: FirebaseAuth = Firebase.auth

// Get the current user
            val user = auth.currentUser
            user?.reload()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (user.isEmailVerified) {
                        // Email is verified, proceed to next activity
                        startActivity(Intent(this, UploadActivity::class.java))
                    } else {
                        // Email is not verified
                        Toast.makeText(this, "Email is not verified", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Reload failed, handle error
                    Toast.makeText(this, "Failed to reload user", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
    private fun openGmail() {
        val intent = packageManager.getLaunchIntentForPackage("com.google.android.gm")

        if (intent != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "Gmail app not installed", Toast.LENGTH_SHORT).show()
        }

    }
}