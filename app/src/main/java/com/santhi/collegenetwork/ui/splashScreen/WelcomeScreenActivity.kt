package com.santhi.collegenetwork.ui.splashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.santhi.collegenetwork.MainActivity
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.ui.auth.OnBordingActivity

class WelcomeScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_screen)
        Handler(mainLooper).postDelayed({
            if (FirebaseAuth.getInstance().currentUser?.uid!=null){
                startActivity(Intent(this,MainActivity::class.java))
            }else {
                startActivity(Intent(this, OnBordingActivity::class.java))
            }
        },2000)
    }
}