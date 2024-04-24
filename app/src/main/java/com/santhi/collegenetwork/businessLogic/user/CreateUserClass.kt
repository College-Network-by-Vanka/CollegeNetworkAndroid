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

    fun createNewUser(email: String,password:String){
        Loading.showAlertDialogForLoading(context)

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {

            if(it.isSuccessful){
                Loading.dismissDialogForLoading()

                context.startActivity(Intent(context,UploadActivity::class.java))
            }
            else {
                Loading.dismissDialogForLoading()
                val errorMessage = it.exception?.message ?: "Unknown error occurred"

                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}