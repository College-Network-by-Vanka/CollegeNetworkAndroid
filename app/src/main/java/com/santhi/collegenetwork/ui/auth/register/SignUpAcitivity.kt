package com.santhi.collegenetwork.ui.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.businessLogic.localStorage.LocalStorageClass
import com.santhi.collegenetwork.businessLogic.otp.GeneratorOtpClass
import com.santhi.collegenetwork.databinding.ActivitySignUpAcitivityBinding

class SignUpAcitivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignUpAcitivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpAcitivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val signUpBtn = binding.verifyBtn
        val genratorOtpClass = GeneratorOtpClass(this)
        signUpBtn.setOnClickListener {
            val email = binding.signupEmail.text.toString()
            val localStorage = LocalStorageClass(this)
            localStorage.saveString("email",email)
            genratorOtpClass.sendOtp(email)

        }
    }
}