package com.santhi.collegenetwork.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.databinding.ActivityOnBordingBinding
import com.santhi.collegenetwork.ui.auth.register.RegisterAccountActivity
import com.santhi.collegenetwork.ui.auth.signIn.SignInActivity

class OnBordingActivity : AppCompatActivity() {
    private lateinit var binding:ActivityOnBordingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBordingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val registerBtn = binding.registerBtn
        val signIn = binding.signIn
        registerBtn.setOnClickListener {
            startActivity(Intent(this,RegisterAccountActivity::class.java))
        }
        signIn.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))

        }
    }
}