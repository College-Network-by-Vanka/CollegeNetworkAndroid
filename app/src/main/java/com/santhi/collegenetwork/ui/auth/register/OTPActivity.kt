package com.santhi.collegenetwork.ui.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.santhi.collegenetwork.businessLogic.localStorage.LocalStorageClass
import com.santhi.collegenetwork.businessLogic.otp.GeneratorOtpClass
import com.santhi.collegenetwork.databinding.ActivityOtpactivityBinding

class OTPActivity : AppCompatActivity() {
    private lateinit var binding:ActivityOtpactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val btn = binding.verifyBtn
        val generatorOtpClass = GeneratorOtpClass(this)

        btn.setOnClickListener {
            val entered_otp = binding.otpEt.text.toString()
            val  localStorageClass = LocalStorageClass(this)
            val otp = localStorageClass.getString("otp")
            if (entered_otp==otp){
                startActivity(Intent(this,PasswordActivity::class.java))
            }else{
                Toast.makeText(this, "Wrong otp!!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.tvResend.setOnClickListener {
            val  localStorageClass = LocalStorageClass(this)
            val getEmail = localStorageClass.getString("email")
            Toast.makeText(this, "otp is sent", Toast.LENGTH_SHORT).show()
            generatorOtpClass.sendOtp(getEmail)
        }
        binding.openMail.setOnClickListener {
            openGmail()
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