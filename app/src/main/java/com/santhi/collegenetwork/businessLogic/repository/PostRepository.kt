package com.santhi.collegenetwork.businessLogic.repository

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.santhi.collegenetwork.businessLogic.localStorage.LocalStorageClass
import com.santhi.collegenetwork.businessLogic.model.NotificationModel
import com.santhi.collegenetwork.businessLogic.notification.NotificationData
import com.santhi.collegenetwork.businessLogic.notification.PushNotifications
import com.santhi.collegenetwork.businessLogic.notification.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class PostRepository(val context: Context) {
  fun  like(postId:String,path:String,firendsId:String,userName:String,postText:String,firendsToken:String){

      val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val database = Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/")
       database.reference.child("likes").child(postId).child(FirebaseAuth.getInstance().currentUser?.uid.toString()).setValue(FirebaseAuth.getInstance().currentUser?.uid).addOnCompleteListener {
           if (it.isSuccessful){
               //Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show()
               database.reference.child(path).child("likes").runTransaction(object : Transaction.Handler {
                   override fun doTransaction(mutableData: MutableData): Transaction.Result {
                       val currentValue = mutableData.getValue(Int::class.java) ?: 0
                       mutableData.value = currentValue + 1
                       return Transaction.success(mutableData)
                   }

                   override fun onComplete(
                       databaseError: DatabaseError?,
                       committed: Boolean,
                       dataSnapshot: DataSnapshot?
                   ) {
                       if (databaseError != null) {
                           Toast.makeText(context, databaseError.message, Toast.LENGTH_SHORT).show()

                       } else if (committed) {
                           val notificationRepository = NotificationRepository()
                           notificationRepository.sendNotificationToFirebase(uid,firendsId,"$userName liked your post",postText,firendsToken,path,context)
                               //Toast.makeText(context, "Post liked successfully!", Toast.LENGTH_SHORT).show()
                       } else {
                           Toast.makeText(context, "Post like was not committed.", Toast.LENGTH_SHORT).show()

                       }
                   }
               })
           }else{
               Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()
           }
       }

        }
    fun checkLiked(postId: String,callback:(Boolean)->Unit){
        val database = Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/")
            database.reference.child("likes").child(postId).child(FirebaseAuth.getInstance().currentUser?.uid.toString()).addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                   if (snapshot.exists()){
                       callback(true)
                   }else{
                       callback(false)
                   }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
    fun removeLike(postId:String,path:String){
        val database = Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/")
        database.reference.child("likes").child(postId).child(FirebaseAuth.getInstance().currentUser?.uid.toString()).removeValue().addOnCompleteListener {
            if (it.isSuccessful){
                //Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show()
                database.reference.child(path).child("likes").runTransaction(object : Transaction.Handler {
                    override fun doTransaction(mutableData: MutableData): Transaction.Result {
                        val currentValue = mutableData.getValue(Int::class.java) ?: 0
                        mutableData.value = currentValue - 1
                        return Transaction.success(mutableData)
                    }

                    override fun onComplete(
                        databaseError: DatabaseError?,
                        committed: Boolean,
                        dataSnapshot: DataSnapshot?
                    ) {
                        if (databaseError != null) {
                            Toast.makeText(context, databaseError.message, Toast.LENGTH_SHORT).show()

                        } else if (committed) {

                            Toast.makeText(context, "Like is removed", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Network issue.", Toast.LENGTH_SHORT).show()

                        }
                    }
                })
            }else{
                Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()
            }
        }
    }



}