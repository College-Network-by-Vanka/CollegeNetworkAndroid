package com.santhi.collegenetwork.ui.editProfile

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.databinding.ActivityEditAccountBinding
import com.santhi.collegenetwork.ui.editProfile.editFragments.ChangeNameFragment
import com.santhi.collegenetwork.ui.editProfile.editFragments.ChangeProfileFragment

class EditAccountActivity : AppCompatActivity() {
    private lateinit var binding:ActivityEditAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val key = intent.getStringExtra("key")
        binding.headText.text = key
        if (key=="Profile"){
            intentFragment(
                R.id.edit_container, ChangeProfileFragment(),this,"ChangeProfileFragment"
            )
        }
        if (key=="Name"){
            intentFragment(
                R.id.edit_container, ChangeNameFragment(),this,"ChangeNameFragment"
            )
        }

        binding.back.setOnClickListener {
            navigateToMainActivity()
        }
        binding.headText.setOnClickListener {
            navigateToMainActivity()
        }
    }
    private fun navigateToMainActivity() {
        finish()

    }
    fun intentFragment(id:Int, fragment: Fragment, context: Context, tag:String){
//        val load = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
//        load.replace(id,fragment)
//        load.commit()

        val transaction: FragmentTransaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
        transaction.replace(id, fragment, tag)
        transaction.addToBackStack(null)
        transaction.commit()

    }
}