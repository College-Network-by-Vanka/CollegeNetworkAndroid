package com.santhi.collegenetwork.businessLogic.Repository

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.santhi.collegenetwork.businessLogic.localStorage.LocalStorageClass
import com.santhi.collegenetwork.businessLogic.model.Clubkey
import com.santhi.collegenetwork.businessLogic.model.PostModel
import com.santhi.collegenetwork.businessLogic.model.UserKey
import com.santhi.collegenetwork.businessLogic.tokenGenrator.TokenManager

class CommunityRepository(private val context:Context) {
    private val database = Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("myclubs")
   private val uid = FirebaseAuth.getInstance().currentUser?.uid



    fun joinCommunity(key:String){
        val data = Clubkey(key)

        if (uid != null) {
            database.child(uid).child(key).setValue(data).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(context, "success", Toast.LENGTH_SHORT).show()
                    addMembersInClub(key)
                }else{
                    Toast.makeText(context, "no", Toast.LENGTH_SHORT).show()

                }
            }
        }

    }
    fun removeCommunity(key: String){
        database.child(uid.toString()).child(key).removeValue().addOnCompleteListener {
            if (it.isSuccessful){
                Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("clubs").child(key).child("members").addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val members = snapshot.getValue(Int::class.java)
                        Toast.makeText(context, "${members}", Toast.LENGTH_SHORT).show()
                        val newVal = members
                        val count = newVal?.minus(1)
                        Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("clubs").child(key).child("members").setValue(count).addOnSuccessListener {
                            Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("members").child(key).child(uid.toString()).removeValue()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
            }
        }
    }
    private fun addMembersInClub(clubName:String){
        Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("members").child(clubName).child(uid.toString()).setValue(
            UserKey(uid)
        ).addOnCompleteListener {
            if (it.isSuccessful){
                Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("clubs").child(clubName).child("members").addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val members = snapshot.getValue(Int::class.java)
                        Toast.makeText(context, "${members}", Toast.LENGTH_SHORT).show()
                        val newVal = members
                        val count = newVal?.plus(1)
                        Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("clubs").child(clubName).child("members").setValue(count)
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
            }
        }
    }
    fun checkMember(clubName:String,callback: (Boolean) -> Unit){
        Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("members").child(clubName).child(uid.toString()).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
              if (snapshot.exists()){
                  callback(true)
              }else{
                  callback(false)
              }
            }

            override fun onCancelled(error: DatabaseError) {
               callback(false)
            }

        })
    }
    fun uploadPost(context: Context,post:PostModel,clubId: String){

        Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("post").child(post.id.toString()).setValue(post).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(context, "Sucessfull", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "noo", Toast.LENGTH_SHORT).show()

            }
        }



    }

//
}