package com.santhi.collegenetwork.businessLogic.otp

import android.content.Context
import android.content.Intent
import com.santhi.collegenetwork.businessLogic.localStorage.LocalStorageClass
import com.santhi.collegenetwork.ui.auth.register.OTPActivity
import com.santhi.collegenetwork.ui.auth.register.PasswordActivity
import kotlinx.coroutines.*
import papaya.`in`.sendmail.SendMail

class GeneratorOtpClass(private val context: Context) {

    fun sendOtp(email: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val localStorage = LocalStorageClass(context)
            val random = (1000..9999).random()
            localStorage.saveString("otp", random.toString())

            // Send OTP via email
           // sendOtpEmail(email, random)
            context.startActivity(Intent(context,PasswordActivity::class.java))
        }
    }

    private suspend fun sendOtpEmail(email: String, otp: Int) {
        withContext(Dispatchers.IO) {
            val mail = SendMail(
                "nicevibe1cr@gmail.com",
                "fcvdhavotsvbmise",
                email,
                "Login Signup app's OTP",
                "Your OTP is -> $otp"
            )
            mail.execute()
        }
    }
}
