package com.santhi.collegenetwork.ui.postUpload

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.databinding.ActivityPostUploadBinding
import com.santhi.collegenetwork.databinding.ActivityUploadBinding
import java.util.UUID

class UploadPostActivity : AppCompatActivity() {
    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            uploadImageToFirebase(it)
        }
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var binding:ActivityPostUploadBinding
    private lateinit var imgUrl:Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostUploadBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        setContentView(binding.root)
        imgUrl = Uri.parse("no")
        binding.progressBar.visibility = View.GONE
        binding.buttonAddImage.setOnClickListener {
            selectImageFromGallery()
        }
        binding.buttonTweet.setOnClickListener {
            val postText = binding.postText.text.toString()

            val url = imgUrl.toString()
            val intent = Intent(this,SelectClubActivity::class.java)
            if (postText.isNotEmpty()&&url != "no"){
               intent.putExtra("type","0")
                intent.putExtra("postText",postText)
                intent.putExtra("url",url)
                startActivity(intent)
            }else if (postText.isEmpty()&&url != "no"){
                intent.putExtra("type","1")
                intent.putExtra("url",url)
                startActivity(intent)
            }else if (postText.isNotEmpty()&&url == "no"){
                intent.putExtra("type","2")
                intent.putExtra("postText",postText)
                startActivity(intent)
            }else{
                Toast.makeText(this, "Empty fileds not allowed!!", Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun selectImageFromGallery() {
        selectImageLauncher.launch("image/*")
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
}