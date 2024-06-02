package com.santhi.collegenetwork.businessLogic.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.businessLogic.repository.PostRepository
import com.santhi.collegenetwork.businessLogic.localStorage.LocalStorageClass
import com.santhi.collegenetwork.businessLogic.model.PostModel
import com.santhi.collegenetwork.databinding.ImgPostBinding
import com.santhi.collegenetwork.ui.home.activity.ReplyActivity
import com.santhi.collegenetwork.ui.home.activity.SeeReplyActivity
import com.santhi.collegenetwork.ui.profile.ProfileActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeAdapter(private val context: Context):RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    var list:List<PostModel> = emptyList()
    private var imgList = HashMap<Int,String>()
    private var nameList = HashMap<Int,String>()

    fun updateList(newList: List<PostModel>){
        list = newList
        notifyDataSetChanged()
    }
    inner class ViewHolder(val binding:ImgPostBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ImgPostBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvPostDescription.visibility = View.VISIBLE
        holder.binding.ivPostImage.visibility = View.VISIBLE
        val postRepository = PostRepository(context)
        val localStorageClass = LocalStorageClass(context)
        val userName = localStorageClass.getString("userName")
        postRepository.checkLiked(list[position].id.toString()){
            holder.binding.likeBtn.isChecked = it

        }
        if (list[position].isAnomyous){
            holder.binding.tvUserName.text = "Anomyous"
           // holder.binding.ivUserAvatar.setBackgroundResource(R.drawable.gost)
            Glide.with(context).load(R.drawable.gost).into(holder.binding.ivUserAvatar)

        }else {



            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val (Name, Img) = getUserName(list[position].authId.toString())
                    holder.binding.tvUserName.text = Name
                    //  holder.binding.tvUserName.text = Name
                    imgList[position] = Img
                    nameList[position] = Name
                    //name = fakeName
                    //  Glide.with(context).load(fakeImg).into(holder.binding.postProfile)


                    loadImageWithProgressBar(Img, holder.binding)
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }

            }
        }
        holder.binding.tvUpvoteCount.text = list[position].likes.toString()
        holder.binding.likeBtn.setOnClickListener {
            val isChecked = holder.binding.likeBtn.isChecked

            if (isChecked) {
                // CheckBox is checked
                // Perform actions when CheckBox is checked

                // CheckBox is unchecked
                // Perform actions when CheckBox is unchecked
                list[position].id?.let { it1 ->
                    list[position].path?.let { it2 ->
                        postRepository.like(it1, it2,list[position].authId.toString(),userName,list[position].postText.toString(),list[position].token.toString())
                    } }

            } else {
                list[position].id?.let { it1 ->
                    list[position].path?.let { it2 ->
                        postRepository.removeLike(it1,it2)
                    }
                }

            }


        }
        holder.binding.tvPostTime.text = getRemainingTime(list[position].time.toString())
        if (list[position].postText.toString() == "noText") {
            holder.binding.tvPostDescription.visibility = View.GONE
        } else {
            holder.binding.tvPostDescription.text = list[position].postText
        }
        if (list[position].postImg.toString() == "NoImg") {
            holder.binding.ivPostImage.visibility = View.GONE
        } else {
            Glide.with(context).load(list[position].postImg)
                .into(holder.binding.ivPostImage)
        }

        holder.binding.root.setOnClickListener {
            val intent = Intent(context,SeeReplyActivity::class.java)
            intent.putExtra("name",nameList[position])
            if (list[position].isAnomyous){
                intent.putExtra("profile", "noProfile")

            }else {
                intent.putExtra("profile", imgList[position])
            }
            intent.putExtra("postText",list[position].postText)
            intent.putExtra("postImg",list[position].postImg)
            intent.putExtra("postId",list[position].id)
            intent.putExtra("comments",list[position].comment.toString())
            intent.putExtra("likes",list[position].likes.toString())
            intent.putExtra("authId",list[position].authId)
            intent.putExtra("path",list[position].path)
            intent.putExtra("date",list[position].time)
            intent.putExtra("token",list[position].token)
            intent.putExtra("community",list[position].clubkey)

            context.startActivity(intent)

        }
        val colorPairs = listOf(
            Pair("#FFFFFF", "#000000"),  // White background with black text
            Pair("#000000", "#FFFFFF"),  // Black background with white text
            Pair("#F44336", "#FFFFFF"),  // Red background with white text
            Pair("#2196F3", "#FFFFFF"),  // Blue background with white text
            Pair("#4CAF50", "#FFFFFF"),  // Green background with white text
            Pair("#FFEB3B", "#000000"),  // Yellow background with black text
            Pair("#9C27B0", "#FFFFFF"),  // Purple background with white text
            Pair("#FF5722", "#FFFFFF"),  // Deep orange background with white text
            Pair("#795548", "#FFFFFF"),  // Brown background with white text
            Pair("#00BCD4", "#FFFFFF"),  // Cyan background with white text
            Pair("#673AB7", "#FFFFFF"),  // Deep purple background with white text
            Pair("#FF9800", "#000000"),  // Orange background with black text
            Pair("#03A9F4", "#000000"),  // Light blue background with black text
            Pair("#8BC34A", "#000000"),  // Light green background with black text
            Pair("#FFC107", "#000000"),  // Amber background with black text
            Pair("#9E9E9E", "#000000"),  // Grey background with black text
            Pair("#607D8B", "#FFFFFF"),  // Blue-grey background with white text
            Pair("#E91E63", "#FFFFFF"),  // Pink background with white text
            Pair("#CDDC39", "#000000"),  // Lime background with black text
            Pair("#2196F3", "#000000")   // Another blue background with black text
        )

        val selectedColorPair = colorPairs.random()
        val backgroundColor = selectedColorPair.first
        val textColor = selectedColorPair.second
        holder.binding.community.setTextColor(Color.parseColor(textColor))
        holder.binding.card.setCardBackgroundColor(Color.parseColor(backgroundColor))
       // holder.binding.card.radius = 100F

        holder.binding.community.text = list[position].clubkey
        holder.binding.tvCommentCount.text = list[position].comment.toString()

       holder.binding.tvReply.setOnClickListener {
           val intent = Intent(context,ReplyActivity::class.java)
           intent.putExtra("name",nameList[position])
           intent.putExtra("postId",list[position].id)
           intent.putExtra("friendId",list[position].authId)

           intent.putExtra("token",list[position].token)
           context.startActivity(intent)
       }
        holder.binding.ivUserAvatar.setOnClickListener {
            if (!list[position].isAnomyous) {
                val intent = Intent(context, ProfileActivity::class.java)
                intent.putExtra("userId", list[position].authId)
                intent.putExtra("token",list[position].token.toString())
                intent.putExtra("profile",imgList[position].toString())
                intent.putExtra("name",nameList[position].toString())


                context.startActivity(intent)
            }else{
                Toast.makeText(context, "You can't see the profile", Toast.LENGTH_SHORT).show()
            }
        }

    }
    suspend fun getProfile(database: DatabaseReference): String =  withContext(Dispatchers.IO) {
        database.child("profile").get().await().value.toString()
    }

    suspend fun getName(database: DatabaseReference): String = withContext(Dispatchers.IO) {
        database.child("name").get().await().value.toString()
    }
    suspend fun getUserName(uid: String): Pair<String, String> = withContext(Dispatchers.IO) {
        val localStorage = LocalStorageClass(context)
        val college =localStorage.getString("college")

        val database = Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .reference
            .child("users")
            .child(uid)

        val fakeImg = getProfile(database)
        val fakeName = getName(database)

        Pair(fakeName, fakeImg)
    }
    fun loadImageWithProgressBar(urlImage: String, binding: ImgPostBinding){
        // Show progress bar

       // binding.shemmer.startShimmer()
        Glide.with(context)
            .load(urlImage)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                  //  binding.shemmer.hideShimmer()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                   // binding.shemmer.hideShimmer()
                    binding.ivUserAvatar.setBackgroundResource(R.drawable.canva_loading)
                    return false
                }

            })
            .into(binding.ivUserAvatar)

    }
    private fun getRemainingTime(dateString: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
         val date = dateFormat.parse(dateString)

        val calendarCurrent = Calendar.getInstance()
        val calendarGiven = Calendar.getInstance()
        if (date != null) {
            calendarGiven.time = date
        }

        val currentTimeInMillis = calendarCurrent.timeInMillis
        val givenTimeInMillis = calendarGiven.timeInMillis

        var remainingTimeInMillis =  currentTimeInMillis- givenTimeInMillis

        if (remainingTimeInMillis <= 0) {
            return "0 seconds"
        }

        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        val remainingDays = remainingTimeInMillis / daysInMilli
        remainingTimeInMillis %= daysInMilli

        val remainingHours = remainingTimeInMillis / hoursInMilli
        remainingTimeInMillis %= hoursInMilli

        val remainingMinutes = remainingTimeInMillis / minutesInMilli
        remainingTimeInMillis %= minutesInMilli

        val remainingSeconds = remainingTimeInMillis / secondsInMilli

        val stringBuilder = StringBuilder()

        if (remainingDays > 0) {
            stringBuilder.append("$remainingDays days, ")
        }

        if (remainingHours > 0 || stringBuilder.isNotEmpty()) {
            stringBuilder.append("$remainingHours hours, ")
        }

        if (remainingMinutes > 0 || stringBuilder.isNotEmpty()) {
            stringBuilder.append("$remainingMinutes minutes, ")
        }

        stringBuilder.append("$remainingSeconds seconds")

        return stringBuilder.toString()
    }
}