package com.santhi.collegenetwork.ui.splashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.ui.auth.OnBordingActivity

class WelcomeScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_screen)
        Handler(mainLooper).postDelayed({
            startActivity(Intent(this,OnBordingActivity::class.java))
        },2000)
    }
}