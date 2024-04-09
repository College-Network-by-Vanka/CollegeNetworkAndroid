package com.santhi.collegenetwork.ui.auth.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.businessLogic.localStorage.LocalStorageClass
import com.santhi.collegenetwork.businessLogic.user.CreateUserClass
import com.santhi.collegenetwork.databinding.ActivityPasswordBinding

class PasswordActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordBinding.inflate(layoutInflater)
        val createUserClass = CreateUserClass(this)
        setContentView(binding.root)
        binding.verifyBtn.setOnClickListener {
            val  password = binding.passwordEt.text.toString()
            val  localStorageClass = LocalStorageClass(this)
            val getEmail = localStorageClass.getString("email")
            createUserClass.createNewUser(getEmail,password)

        }
    }
}