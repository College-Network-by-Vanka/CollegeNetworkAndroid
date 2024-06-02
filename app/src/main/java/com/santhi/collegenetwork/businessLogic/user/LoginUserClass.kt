package com.santhi.collegenetwork.businessLogic.user

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.santhi.collegenetwork.MainActivity
import com.santhi.collegenetwork.businessLogic.loading.Loading.dismissDialogForLoading
import com.santhi.collegenetwork.businessLogic.loading.Loading.showAlertDialogForLoading
import com.santhi.collegenetwork.businessLogic.localStorage.LocalStorageClass
import com.santhi.collegenetwork.businessLogic.model.User
import com.santhi.collegenetwork.businessLogic.tokenGenrator.TokenManager
import com.santhi.collegenetwork.ui.auth.PermissionActivity

class LoginUserClass (val context: Context) {
    val auth = FirebaseAuth.getInstance()
    fun loginUser(email:String,password:String){
        val tokenManager = TokenManager(context)
        tokenManager.saveTokenLocally()


        showAlertDialogForLoading(context)

        auth.signInWithEmailAndPassword(email.trim(),password.trim()).addOnCompleteListener {
            if (it.isSuccessful){
                val uid = FirebaseAuth.getInstance().currentUser?.uid
             //   Toast.makeText(context, "$uid", Toast.LENGTH_SHORT).show()
                Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("users").child(uid.toString()).addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val getData = snapshot.getValue(User::class.java)
                        val name = getData?.name
                        val gender = getData?.gender
                        val college = getData?.college
                        val year = getData?.year
                        val branch = getData?.branch
                        val sec = getData?.sec
                        val userId = getData?.userId
                        val notification = getData?.notification
                        val profile = getData?.profile
                       // Toast.makeText(context, "$snapshot", Toast.LENGTH_SHORT).show()

                        val localStorage= LocalStorageClass(context)

                        localStorage.saveString("userName",name.toString())

                        localStorage.saveString("email",email)
                        localStorage.saveString("gender",gender.toString())
                        localStorage.saveString("college",college.toString())

                        localStorage.saveString("year",year.toString())
                        localStorage.saveString("branch",branch.toString())
                        localStorage.saveString("sec",sec.toString())
                     localStorage.saveString("profile",profile.toString())
                        val path = userId
                        val token = tokenManager.getSavedToken()
                        Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("users").child(path.toString()).child("notification").setValue(token).addOnCompleteListener {
                            if (it.isSuccessful){
                                dismissDialogForLoading()


                                context.startActivity(Intent(context,PermissionActivity::class.java))
                            }else{
                                Toast.makeText(context, "network issue", Toast.LENGTH_SHORT).show()
                            }
                        }


                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })

                //context.startActivity(Intent(context, MainActivity::class.java))
            }else{
                dismissDialogForLoading()
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}