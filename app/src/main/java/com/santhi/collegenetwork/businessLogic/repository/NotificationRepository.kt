package com.santhi.collegenetwork.businessLogic.repository

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
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

class NotificationRepository {
    fun seen(userId: String, notificationId:String){
        val DATABASE = Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        DATABASE.child("notifications").child(userId).child(notificationId).child("seen").setValue(true).addOnSuccessListener {

        }
    }
     fun sendNotificationToFirebase(userId: String, friendsId: String , title: String, message: String, token: String,path:String,context:Context,isChat:Boolean=false) {
         if (userId != friendsId) {


             val localStorage = LocalStorageClass(context)
             val college = localStorage.getString("college")
             val userPath = userId
             val currentDateTime = LocalDateTime.now()
             val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

             val formattedDateTime = currentDateTime.format(formatter)
             val notificationId = UUID.randomUUID().toString()
             val data =
                 NotificationModel(
                     notificationId,
                     title,
                     message,
                     path,
                     userPath,
                     formattedDateTime
                 )
             if (!isChat) {
                 val DATABASE =
                     Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child(
                         "notifications"
                     )
                 DATABASE.child(friendsId).child(notificationId).setValue(data)
                     .addOnSuccessListener {
                         // latSendNotification(userId,token)
                         PushNotifications(
                             NotificationData(title, message),
                             token
                         ).also {
                             sendNotification(it, context)
                         }

                     }
             } else {
//             Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child(
//                 "chatsAlerts"
//             ).child(userId).setValue()
                 val database =
                     Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/")

                 //Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show()
                 database.reference.child("chatsAlerts").child(friendsId)
                     .runTransaction(object : Transaction.Handler {
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
                                 Toast.makeText(
                                     context,
                                     databaseError.message,
                                     Toast.LENGTH_SHORT
                                 ).show()

                             } else if (committed) {
                                 PushNotifications(
                                     NotificationData(title, message),
                                     token
                                 ).also {
                                     sendNotification(it, context)
                                 }
                             } else {
                                 Toast.makeText(
                                     context,
                                     "Post like was not committed.",
                                     Toast.LENGTH_SHORT
                                 ).show()

                             }
                         }
                     })


             }
         }
     }
     fun sendNotification(notification: PushNotifications,context: Context) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    // showToast("Notification sent successfully")
                } else {
                    showToast("Failed to send notification: ${response.errorBody()?.string()}",context)
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                showToast("Error: ${e.message}",context)
            }
        }
    }

    private fun showToast(message: String,context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}