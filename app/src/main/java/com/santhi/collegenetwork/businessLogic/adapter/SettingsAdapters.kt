package com.santhi.collegenetwork.businessLogic.adapter


import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.santhi.collegenetwork.databinding.SettingItemBinding
import com.santhi.collegenetwork.ui.auth.OnBordingActivity
import com.santhi.collegenetwork.ui.editProfile.EditAccountActivity
import com.santhi.collegenetwork.ui.myFriends.MyFriendsActivity
import com.santhi.collegenetwork.ui.webView.WebViewActivity


class SettingsAdapters(
  val settingsData:List<Pair<String,String>>,
    val context: Context

):RecyclerView.Adapter<SettingsAdapters.ViewHolder>() {




    class ViewHolder(var binding: SettingItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(SettingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val lightGray = Color.parseColor("#ECEFF1")
        val purpleColor = Color.parseColor("#9c27b0")
        val (currentKey, currentValue) = settingsData[position]

        if (currentKey == "My Account" || currentKey == "Support" || currentKey == "Company" || currentKey == "Follow Us" || currentKey == "Premium Mode") {
            // Handle specific keys with different UI
            holder.binding.settingTvValue.visibility = View.GONE
            holder.binding.settingsNameTv.text = currentKey
            holder.binding.settingsNameTv.setTextColor(purpleColor)
            holder.binding.settingsCard.setCardBackgroundColor(lightGray)
        } else if (!(currentKey.contains("Name"))) {
            // Hide value and set only the key
            holder.binding.settingTvValue.visibility = View.GONE
            holder.binding.settingsNameTv.text = currentKey
            holder.binding.settingsNameTv.setTextColor(Color.BLACK) // Set default text color
            holder.binding.settingsCard.setCardBackgroundColor(Color.WHITE) // Set default card color
        } else {
            // For other cases, show both key and value
            holder.binding.settingsNameTv.text = currentKey
            holder.binding.settingTvValue.text = currentValue
            holder.binding.settingTvValue.visibility = View.VISIBLE // Ensure value is visible
            holder.binding.settingsNameTv.setTextColor(Color.BLACK) // Reset text color
            holder.binding.settingsCard.setCardBackgroundColor(Color.WHITE) // Reset card color
        }
        holder.binding.settingsCard.setOnClickListener {
            if ((position==5) || (position == 6) || (position == 7) || (position == 9) || (position == 10) || (position == 11) || (position == 11) ){

               openLinkInChrome(currentValue)
            }
            val  intent = Intent(context, EditAccountActivity::class.java)
            if (currentKey=="Profile"){
              intent.putExtra("key","Profile")
                context.startActivity(intent)
            }
            if (currentKey=="Name"){
                intent.putExtra("key","Name")
                context.startActivity(intent)
            }


            if(currentKey=="Twitter"){
              openTwitterProfile()
            }
            if (currentKey=="Friends"){
                context.startActivity(Intent(context,MyFriendsActivity::class.java))
            }
            if(currentKey=="Instagram"){
                openInstagramProfile()
            }
            if(currentKey=="Website"){
                openLinkInChrome(currentValue)
            }
            if (currentKey=="LinkedIn"){
                openLinkedInPage()
            }
            if (currentKey == "Logout"){
                AlertDialog.Builder(context)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes") { dialog, which ->
                        FirebaseAuth.getInstance().signOut()
                        val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            clear()
                            apply()
                        }
                        context.startActivity(Intent(context,OnBordingActivity::class.java))
                    }
                    .setNegativeButton("No") { dialog, which ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()



            }
        }






    }


    override fun getItemCount(): Int {
        return  settingsData.size
    }
    private fun openLinkInChrome(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            // Specify the Chrome package name
            setPackage("com.android.chrome")
        }
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {

            // Chrome is probably not installed; handle the error here
            Toast.makeText(context, "Chrome is not installed. Please install it and try again.", Toast.LENGTH_SHORT).show()
        }
    }
  private  fun openInstagramProfile() {
        val instagramProfile = "https://www.instagram.com/gossylovesyou/"
        val appUri = Uri.parse(instagramProfile)
        val intent = Intent(Intent.ACTION_VIEW)

        intent.data = if (isAppInstalled("com.instagram.android")) {
            appUri
        } else {
            Uri.parse(instagramProfile)
        }

        intent.setPackage("com.instagram.android")

        context.startActivity(intent)
    }
   private fun openTwitterProfile() {
        val twitterProfile = "https://twitter.com/gossylovesyou"
        val appUri = Uri.parse(twitterProfile)
        val intent = Intent(Intent.ACTION_VIEW)

        intent.data = if (isAppInstalled("com.twitter.android")) {
            appUri
        } else {
            Uri.parse(twitterProfile)
        }

        intent.setPackage("com.twitter.android")

        context.startActivity(intent)
    }
    private fun isAppInstalled(packageName: String): Boolean {
        val pm = context.packageManager
        return try {
            pm.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
   private fun openLinkedInPage() {
        val linkedInProfile = "https://www.linkedin.com/company/gossy-the-perfect-social-app/about/?viewAsMember=true"
        val appUri = Uri.parse(linkedInProfile)
        val intent = Intent(Intent.ACTION_VIEW)

        intent.data = if (isAppInstalled("com.linkedin.android")) {
            appUri
        } else {
            Uri.parse(linkedInProfile)
        }

        intent.setPackage("com.linkedin.android")

        context.startActivity(intent)
    }
}