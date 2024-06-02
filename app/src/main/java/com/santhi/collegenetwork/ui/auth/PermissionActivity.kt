package com.santhi.collegenetwork.ui.auth

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.santhi.collegenetwork.MainActivity

import com.santhi.collegenetwork.databinding.ActivityPermissionBinding

class PermissionActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPermissionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(

                Manifest.permission.POST_NOTIFICATIONS,

            )
        } else {
            arrayOf(




            )
        }
        val notGrantedPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()
        if (notGrantedPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, notGrantedPermissions, 111)


        }
        binding.upload.setOnClickListener {

            startActivity(Intent(this, MainActivity::class.java))

        }
        binding.back.setOnClickListener {
            finish()
        }


    }
}