package com.santhi.collegenetwork.ui.editProfile.editFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.santhi.collegenetwork.MainActivity
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.businessLogic.loading.Loading
import com.santhi.collegenetwork.businessLogic.localStorage.LocalStorageClass
import com.santhi.collegenetwork.databinding.FragmentChangeNameBinding


class ChangeNameFragment : Fragment() {

    private lateinit var binding:FragmentChangeNameBinding
    private lateinit var localStorageClass: LocalStorageClass
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangeNameBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        localStorageClass = LocalStorageClass(requireContext())

        binding.upload.visibility = View.INVISIBLE

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navigateToMainActivity()
        }


        binding.changeNameEt.addTextChangedListener { editable ->
            if (!editable.isNullOrEmpty()) {
                binding.upload.visibility = View.VISIBLE
            } else {
                binding.upload.visibility = View.INVISIBLE
            }
        }
        binding.upload.setOnClickListener {
            Loading.showAlertDialogForLoading(requireContext())
            updateName(binding.changeNameEt.text.toString())
        }
        return binding.root

    }
    private fun updateName(name:String){
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val database = Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("users")
        database.child(uid.toString()).child("name").setValue(name).addOnSuccessListener {
            Toast.makeText(requireContext(), "Name is updated!!", Toast.LENGTH_SHORT).show()
            localStorageClass.saveString("userName",name)
            Loading.dismissDialogForLoading()

        }
    }
    private fun navigateToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)

    }
}