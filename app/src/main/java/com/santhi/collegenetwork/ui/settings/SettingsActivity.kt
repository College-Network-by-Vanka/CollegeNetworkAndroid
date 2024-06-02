package com.santhi.collegenetwork.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.businessLogic.adapter.SettingsAdapters
import com.santhi.collegenetwork.businessLogic.localStorage.LocalStorageClass
import com.santhi.collegenetwork.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val storeManager = LocalStorageClass(this)
        val name = storeManager.getString("userName", "")
        val profile = storeManager.getString("profile", "")
        binding.back.setOnClickListener {
            finish()
        }

        val settingsList: List<Pair<String, String>> = listOf(
            "Friends" to "",
            "My Account" to "",
            "Name" to name,
            "Profile" to profile,
            "Support" to "",
            "Help Center" to "https://gossy.vercel.app/help-center",
            "Terms of Service" to "https://gossy.vercel.app/terms-of-service",
            "Privacy Policy" to "https://gossy.vercel.app/privacy-policy",
            "Company" to "",
            "About" to "https://gossy.vercel.app/about",
            "Company Blogs" to "https://gossy.vercel.app/blogs",
            "Careers" to "https://gossy.vercel.app/careers",
            "Feedback" to "https://gossy.vercel.app/contact",
            "Follow Us" to "",
            "Twitter" to "https://twitter.com/gossylovesyou",
            "Instagram" to "https://www.instagram.com/collegenetworkbyvank/",
            "LinkedIn" to "https://www.linkedin.com/company/college-network-by-vanka/about/?viewAsMember=true",
            "Website" to "https://collegenetwork.vercel.app/",
            "Logout" to ""






        )
        binding.settingsRv.addItemDecoration(
            DividerItemDecoration(
                binding.settingsRv.context,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.settingsRv.layoutManager = LinearLayoutManager(this)
        binding.settingsRv.adapter = SettingsAdapters(settingsList, this)
    }
}


