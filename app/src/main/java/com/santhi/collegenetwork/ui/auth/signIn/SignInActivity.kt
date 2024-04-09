package com.santhi.collegenetwork.ui.auth.signIn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.businessLogic.localStorage.LocalStorageClass
import com.santhi.collegenetwork.businessLogic.user.LoginUserClass
import com.santhi.collegenetwork.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val loginUserClass = LoginUserClass(this)
        binding.verifyBtn.setOnClickListener {
            val  email = binding.emailEt.text.toString()
            val  password = binding.passwordEt.text.toString()
            loginUserClass.loginUser(email,password )


        }

    }
}