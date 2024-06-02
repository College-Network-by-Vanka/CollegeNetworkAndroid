package com.santhi.collegenetwork.ui.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.businessLogic.localStorage.LocalStorageClass
import com.santhi.collegenetwork.databinding.ActivityRegisterAccountBinding

class RegisterAccountActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val localStorage = LocalStorageClass(this)

        // Setting click listener for the 'Next' button
        binding.next.setOnClickListener {
            val name = binding.nameEt.text.toString()
            val gender = binding.gender.selectedItem.toString()
            val college = binding.college.selectedItem.toString()

            if (validateInputs(name, gender, college)) {
                // Saving user details to local storage
                localStorage.saveString("userName", name)
                localStorage.saveString("gender", gender)
                localStorage.saveString("college", college)

                // Starting MainActivity after registration
                startActivity(Intent(this, FormActivity::class.java))
            }
        }
        binding.imageButton.setOnClickListener {
            finish()
        }

    }
    private fun validateInputs(name: String, gender: String, college: String): Boolean {
        if (name.trim().length < 4) {
            showToast("Enter your full name")
            return false
        }

        if (gender == "Select Gender") {
            showToast("Select Gender")
            return false
        }
        if (college == "Select your college") {
            showToast("Select your college")
            return false
        }
        return true
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}