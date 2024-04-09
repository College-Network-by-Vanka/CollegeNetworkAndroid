package com.santhi.collegenetwork.businessLogic.user

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.santhi.collegenetwork.MainActivity
import com.santhi.collegenetwork.businessLogic.loading.Loading
import com.santhi.collegenetwork.businessLogic.localStorage.LocalStorageClass
import com.santhi.collegenetwork.businessLogic.otp.GeneratorOtpClass
import com.santhi.collegenetwork.businessLogic.upload.UploadActivity

class CreateUserClass(val context: Context) {
   private val auth = FirebaseAuth.getInstance()
    private val genrateOtpClass = GeneratorOtpClass(context)
    fun createNewUser(email: String,password:String){
        Loading.showAlertDialogForLoading(context)

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {

            if(it.isSuccessful){
                Loading.dismissDialogForLoading()

                genrateOtpClass.sendOtp(email)
                context.startActivity(Intent(context,MainActivity::class.java))
            }
            else {
                Loading.dismissDialogForLoading()
                val errorMessage = it.exception?.message ?: "Unknown error occurred"

                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}