package com.santhi.collegenetwork.ui.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.businessLogic.localStorage.LocalStorageClass
import com.santhi.collegenetwork.databinding.ActivityFormBinding

class FormActivity : AppCompatActivity() {
    private lateinit var binding:ActivityFormBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val localStorage = LocalStorageClass(this)

        binding.verifyBtn.setOnClickListener {
            // Retrieve selected items from spinners
            val selectYear = binding.batchSpinner.selectedItem.toString()
            val selectBranch = binding.branchSpinner.selectedItem.toString()
            val selectSection = binding.sections.selectedItem.toString()

            // Check if any of the selections are not made
            if (selectYear == "Select your passout year") {
                showToast("Select year")
                return@setOnClickListener
            }
            if (selectBranch == "Select your branch") {
                showToast("Select branch")
                return@setOnClickListener
            }
            if (selectSection == "Select your section") {
                showToast("Select section")
                return@setOnClickListener
            }

            // All selections are valid, proceed to save them
            localStorage.saveString("year", selectYear)
            localStorage.saveString("branch", selectBranch)
            localStorage.saveString("sec", selectSection)
            startActivity(Intent(this,UploadProfileActivity::class.java))
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}