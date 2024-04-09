package com.santhi.collegenetwork.ui.auth.register

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.firebase.storage.FirebaseStorage
import com.santhi.collegenetwork.MainActivity
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.businessLogic.loading.Loading.dismissDialogForLoading
import com.santhi.collegenetwork.businessLogic.loading.Loading.showAlertDialogForLoading
import com.santhi.collegenetwork.businessLogic.localStorage.LocalStorageClass
import com.santhi.collegenetwork.businessLogic.tokenGenrator.TokenManager
import com.santhi.collegenetwork.databinding.ActivityUploadBinding
import com.santhi.collegenetwork.databinding.ActivityUploadProfileBinding
import java.util.UUID

class UploadProfileActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUploadProfileBinding
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference
    private lateinit var myProfile: String
    private lateinit var selectedImgUri: Uri
    private var permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            var isGranted = true
            for (item in map) {
                if (!item.value) {
                    isGranted = false
                }
            }
            if (isGranted) {
                openGallery() // If permissions granted, open the gallery to pick an image
            } else {
                Toast.makeText(this, "Permission is denied", Toast.LENGTH_SHORT).show()
                openAppSettings(this)
            }
        }

    // Pick image result launcher
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val selectedImageUri = data?.data
                binding.circleImageView.setImageURI(selectedImageUri)
                if (selectedImageUri != null) {
                    selectedImgUri = selectedImageUri

                } else {
                    Toast.makeText(this, "Select again", Toast.LENGTH_SHORT).show()
                }
                //    uploadImageToFirebase(selectedImageUri, requireContext())
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        selectedImgUri = Uri.EMPTY
        val tokenManager = TokenManager(this)
        tokenManager.saveTokenLocally()
        binding.goGalleryBtn.setOnClickListener {
            readPermission()
        }
        binding.changeProfile.setOnClickListener {
            readPermission()
        }
        binding.circleImageView.setOnClickListener {
            readPermission()
        }
        binding.upload.setOnClickListener {
            uploadImageToFirebase(selectedImgUri, this)
        }
    }
    fun uploadImageToFirebase(imageUri: Uri, requireContext: Context) {
        val localStorage = LocalStorageClass(this)
        val uuid = UUID.randomUUID().toString()


        showAlertDialogForLoading(this)


        // Create a storage reference for the image with a unique name
        val storageRef = storageReference.child("profile/${UUID.randomUUID()}")

        // Upload the image file to Firebase Storage
        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                // Once the image is successfully uploaded, get its download URL
                storageRef.downloadUrl.addOnSuccessListener { imgLink ->
                    myProfile = imgLink.toString()
                    //  Loading.dismissDialogForLoading()
                    // Glide.with(requireContext).load(imgLink).into(binding.circleImageView)

                    localStorage.saveString("profile", imgLink.toString())
                    updateProfile(uuid, imgLink.toString())


                }
            }
            .addOnFailureListener {
                // If image upload fails, show an error message
                Toast.makeText(requireContext, it.message, Toast.LENGTH_SHORT).show()
                dismissDialogForLoading()
            }
    }

    private fun readPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (!hasPermissionCheck(permission[0])) {
            permissionLauncher.launch(permission)
        } else {
            openGallery() // If permission already granted, open the gallery

        }
    }

    // Method to check if a permission is granted
    private fun hasPermissionCheck(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
    }

    private fun updateProfile(number: String, profile: String) {
//        Loading.showAlertDialogForLoading(requireContext())
//        storeManager.saveString("profile",profile)
//        val database =
//            Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child(
//                "users"
//            )
//        database.child(number).child("profile").setValue(profile).addOnSuccessListener {
        Toast.makeText(this, "Profile is updated!!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this,SignUpAcitivity::class.java))
        dismissDialogForLoading()
        // }
    }

    fun openAppSettings(context: Context) {
        Toast.makeText(this, "give permission", Toast.LENGTH_SHORT).show()
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        startActivityForResult(intent, 10001)
    }

//    private fun navigateToMainActivity() {
//        val intent = Intent(this, SignUpActivity::class.java)
//        startActivity(intent)
//
//    }



}