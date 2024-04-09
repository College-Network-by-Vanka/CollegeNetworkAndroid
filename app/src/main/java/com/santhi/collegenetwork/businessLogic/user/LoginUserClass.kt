package com.santhi.collegenetwork.businessLogic.user

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.santhi.collegenetwork.MainActivity
import com.santhi.collegenetwork.businessLogic.loading.Loading.dismissDialogForLoading
import com.santhi.collegenetwork.businessLogic.loading.Loading.showAlertDialogForLoading
import com.santhi.collegenetwork.businessLogic.localStorage.LocalStorageClass

class LoginUserClass (val context: Context) {
    val auth = FirebaseAuth.getInstance()
    fun loginUser(email:String,password:String){
        showAlertDialogForLoading(context)
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            if (it.isSuccessful){

                dismissDialogForLoading()
                Toast.makeText(context, "Good job!", Toast.LENGTH_SHORT).show()
                context.startActivity(Intent(context, MainActivity::class.java))
            }else{
                dismissDialogForLoading()
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}