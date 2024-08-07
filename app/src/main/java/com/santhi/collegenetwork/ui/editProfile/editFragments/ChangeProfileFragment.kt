package com.santhi.collegenetwork.ui.editProfile.editFragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.santhi.collegenetwork.MainActivity
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.businessLogic.loading.Loading
import com.santhi.collegenetwork.businessLogic.localStorage.LocalStorageClass
import com.santhi.collegenetwork.databinding.FragmentChangeProfileBinding
import java.util.UUID


class ChangeProfileFragment : Fragment() {
    private lateinit var binding: FragmentChangeProfileBinding
    private lateinit var localStorageClass: LocalStorageClass
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
                Toast.makeText(requireContext(), "Permission is denied", Toast.LENGTH_SHORT).show()
                openAppSettings(requireContext())
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
                    Toast.makeText(requireContext(), "Select again", Toast.LENGTH_SHORT).show()
                }
                //    uploadImageToFirebase(selectedImageUri, requireContext())
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        selectedImgUri = Uri.EMPTY
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navigateToMainActivity()
        }
        binding = FragmentChangeProfileBinding.inflate(layoutInflater, container, false)
        localStorageClass = LocalStorageClass(requireContext())
        val profile = localStorageClass.getString("profile", "")
        myProfile = profile
        val name = localStorageClass.getString("userName", "")

        Glide.with(requireActivity()).load(profile).into(binding.circleImageView)

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
            uploadImageToFirebase(selectedImgUri, requireContext())
        }
        return binding.root
    }


    fun uploadImageToFirebase(imageUri: Uri, requireContext: Context) {

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        Loading.showAlertDialogForLoading(requireContext())


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

                    localStorageClass.saveString("profile", imgLink.toString())
                    updateProfile(uid.toString(), imgLink.toString())


                }
            }
            .addOnFailureListener {
                // If image upload fails, show an error message
                Toast.makeText(requireContext, it.message, Toast.LENGTH_SHORT).show()
                Loading.dismissDialogForLoading()
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
            requireContext(),
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
        val database =
            Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child(
                "users"
            )
        database.child(number).child("profile").setValue(profile).addOnSuccessListener {
            Toast.makeText(requireContext(), "Profile is updated!!", Toast.LENGTH_SHORT).show()
            Loading.dismissDialogForLoading()
        }
    }

    fun openAppSettings(context: Context) {
        Toast.makeText(requireContext(), "give permission", Toast.LENGTH_SHORT).show()
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        startActivityForResult(intent, 10001)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)

    }


}