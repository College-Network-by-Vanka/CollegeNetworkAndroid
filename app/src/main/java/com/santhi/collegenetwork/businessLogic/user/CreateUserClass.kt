package com.santhi.collegenetwork.businessLogic.user

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.santhi.collegenetwork.MainActivity
import com.santhi.collegenetwork.businessLogic.loading.Loading
import com.santhi.collegenetwork.businessLogic.localStorage.LocalStorageClass
import com.santhi.collegenetwork.businessLogic.otp.GeneratorOtpClass
import com.santhi.collegenetwork.businessLogic.upload.UploadActivity
import com.santhi.collegenetwork.ui.auth.register.VerificationActivity

class CreateUserClass(val context: Context) {
   private val auth = FirebaseAuth.getInstance()

    fun createNewUser(email: String,password:String){
        Loading.showAlertDialogForLoading(context)


        auth.createUserWithEmailAndPassword(email.trim(),password.trim()).addOnCompleteListener {

            if(it.isSuccessful){
                val auth: FirebaseAuth = Firebase.auth

// Get the current user
                val user = auth.currentUser

// Send email verification
                user?.sendEmailVerification()
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Loading.dismissDialogForLoading()
                            context.startActivity(Intent(context,VerificationActivity::class.java))


                        } else {
                            Toast.makeText(context, "noo", Toast.LENGTH_SHORT).show()
                        }
                    }

            }
            else {
                Loading.dismissDialogForLoading()
                val errorMessage = it.exception?.message ?: "Unknown error occurred"

                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}