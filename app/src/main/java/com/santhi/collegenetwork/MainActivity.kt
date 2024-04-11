 package com.santhi.collegenetwork

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.santhi.collegenetwork.businessLogic.localStorage.LocalStorageClass
import com.santhi.collegenetwork.databinding.ActivityMainBinding
import com.santhi.collegenetwork.ui.community.CommunityFragment
import com.santhi.collegenetwork.ui.home.fragment.HomeFragment

 class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val localStorageClass = LocalStorageClass(this)
        val profile = localStorageClass.getString("profile")
        val bottomNav = binding.bottomNavigationView
        Glide.with(this).load(profile).into(binding.profile)
        bottomNav.setOnItemSelectedListener { menu->
            when(menu.itemId){
                R.id.home->{
                    loadFragment(
                        HomeFragment(),
                        true
                    )
                    true
                }
                R.id.community->{
                    loadFragment(
                    CommunityFragment(),
                    true
                    )
                    true
                }
                else->true

            }

        }
    }
     fun loadFragment(
         fragment: Fragment,
         addToBackStack: Boolean = false
     ) {
         val transaction = supportFragmentManager.beginTransaction()
         transaction.add(R.id.frag, fragment)

         if (addToBackStack) {
             transaction.addToBackStack(null)
         }

         transaction.commit()
     }
}