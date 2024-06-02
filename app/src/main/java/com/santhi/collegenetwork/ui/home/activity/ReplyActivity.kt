package com.santhi.collegenetwork.ui.home.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.santhi.collegenetwork.businessLogic.repository.PostRepository
import com.santhi.collegenetwork.businessLogic.localStorage.LocalStorageClass
import com.santhi.collegenetwork.businessLogic.model.PostModel
import com.santhi.collegenetwork.businessLogic.repository.NotificationRepository
import com.santhi.collegenetwork.businessLogic.tokenGenrator.TokenManager
import com.santhi.collegenetwork.databinding.ActivityReplyBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class ReplyActivity : AppCompatActivity() {
    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            uploadImageToFirebase(it)
        }
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var imgUrl: Uri
    private lateinit var binding:ActivityReplyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReplyBinding.inflate(layoutInflater)
        binding.progressBar.visibility = View.GONE
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        setContentView(binding.root)
        val name = intent.getStringExtra("name")
        val postId = intent.getStringExtra("postId")
        val friendsId = intent.getStringExtra("friendId")
        val token = intent.getStringExtra("token")


        binding.name.text = "replying to @"+name
        imgUrl = Uri.parse("NoImg")
        binding.buttonAddImage.setOnClickListener {
            selectImageFromGallery()
        }
        binding.buttonTweet.setOnClickListener {
            commentPost(postId.toString(),false,friendsId.toString(),token.toString())
        }
        binding.anonymouslyButtonTweet.setOnClickListener {
            commentPost(postId.toString(),true,friendsId.toString(),token.toString())
        }

        
    }
    fun commentPost(postId:String,isAnomyous:Boolean,friendsId:String,tokenFriend:String){
        val  postRepository = PostRepository(this)
        val parentPath = "post/${postId}/comment"
        val localStorageClass = LocalStorageClass(this)
        val userName = localStorageClass.getString("userName")
        val tokenManager = TokenManager(this)
        var postText = "noText"
        if (binding.postText.text.toString().isNotEmpty()){
             postText = binding.postText.text.toString()
        }
        val notificationRepository = NotificationRepository()
        val id = UUID.randomUUID().toString()
        val uid = FirebaseAuth.getInstance().currentUser?.uid
      val token = tokenManager.getSavedToken()
        val path = "comments/${postId}/${id}"
        val title ="${userName.toString()} commented on your post"
        val data = PostModel(postText,imgUrl.toString(),0,0,getCurrentDateTime(),id,uid,token,path,"comment",isAnomyous)
        Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("comments").child(postId).child(id)
            .setValue(data).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show()
                   notificationRepository.sendNotificationToFirebase(uid.toString(),friendsId,title,postText.toString(),tokenFriend.toString(),path,this)
                    finish()
                }else{
                    Toast.makeText(this, "Try again", Toast.LENGTH_SHORT).show()
                }
            }
        likeIncrement(parentPath)

    }
    private fun likeIncrement(path:String){
        val ref = Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child(path)
        ref.runTransaction(object : Transaction.Handler {
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
                    println("Firebase increment transaction error: ${databaseError.message}")
                } else {
                    println("Increment successful? $committed")
                    println("New value after increment: ${dataSnapshot?.value}")
                }
            }
        })

    }
    private fun uploadImageToFirebase(imageUri: Uri) {
        binding.progressBar.visibility = View.VISIBLE
        val user = auth.currentUser
        user?.let {
            val userId = it.uid
            val storageRef = storageReference.child("images/$userId/${UUID.randomUUID()}")

            storageRef.putFile(imageUri)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener {
                        imgUrl = it
                        binding.progressBar.visibility = View.GONE
                        Glide.with(this).load(imageUri).into(binding.imageViewPreview)
                    }
                }

                .addOnFailureListener {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
                }
        }
    }
    private fun selectImageFromGallery() {
        selectImageLauncher.launch("image/*")
    }
    fun getCurrentDateTime(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return currentDateTime.format(formatter)
    }
}