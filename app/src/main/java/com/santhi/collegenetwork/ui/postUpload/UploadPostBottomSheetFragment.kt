package com.santhi.collegenetwork.ui.postUpload

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.databinding.UploadBottomSheetFragmentBinding

class UploadPostBottomSheetFragment: BottomSheetDialogFragment() {
    private lateinit var binding:UploadBottomSheetFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = UploadBottomSheetFragmentBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        binding.btnUploadPost.setOnClickListener {
        startActivity(Intent(requireContext(),UploadPostActivity::class.java))
        }
        return binding.root
    }
    fun loadFragment(
        fragment: Fragment,
        addToBackStack: Boolean = false
    ) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.frag, fragment)

        if (addToBackStack) {
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }
}