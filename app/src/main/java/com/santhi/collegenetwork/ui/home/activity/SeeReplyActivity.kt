package com.santhi.collegenetwork.ui.home.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.santhi.collegenetwork.businessLogic.repository.PostRepository
import com.santhi.collegenetwork.businessLogic.adapter.HomeAdapter
import com.santhi.collegenetwork.businessLogic.localStorage.LocalStorageClass
import com.santhi.collegenetwork.businessLogic.model.PostModel
import com.santhi.collegenetwork.businessLogic.viewModel.CommentsViewModel
import com.santhi.collegenetwork.databinding.ActivitySeeReplyBinding
import com.santhi.collegenetwork.ui.profile.ProfileActivity

class SeeReplyActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySeeReplyBinding
    private lateinit var viewModel:CommentsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[CommentsViewModel::class.java]
        binding = ActivitySeeReplyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.shimmer.startShimmer()
        binding.buttonBack.setOnClickListener {
            finish()
        }

        val localStorageClass = LocalStorageClass(this)
        val userName = localStorageClass.getString("userName")
        val postRepository = PostRepository(this)
        val isNotification = intent.getBooleanExtra("isNotification",false)
        if (!isNotification) {

            val name = intent.getStringExtra("name")
            val profile = intent.getStringExtra("profile")
            val postText = intent.getStringExtra("postText")
            val postImg = intent.getStringExtra("postImg")
            val postId = intent.getStringExtra("postId")
            val authId = intent.getStringExtra("authId")
            val comments = intent.getStringExtra("comments")
            val likes = intent.getStringExtra("likes")
            val community = intent.getStringExtra("community")
            val tokenget = intent.getStringExtra("token")
            binding.tvReply.setOnClickListener {

                val intent = Intent(this,ReplyActivity::class.java)
                intent.putExtra("name",name)
                intent.putExtra("postId",postId)

                intent.putExtra("token",tokenget)
                startActivity(intent)

            }
            binding.ivUserAvatar.setOnClickListener {
                val intent = Intent(this, ProfileActivity::class.java)
                intent.putExtra("userId", authId)
                intent.putExtra("token",tokenget.toString())
                intent.putExtra("profile",profile)
                intent.putExtra("name",name)


                startActivity(intent)
            }
            binding.community.text = community
            binding.tvUpvoteCount.text = likes
            binding.tvCommentCount.text = comments
            val date = intent.getStringExtra("date")
            val token = intent.getStringExtra("token")
            val path = intent.getStringExtra("path")

            viewModel.fetchDataFromDatabase(postId.toString())
            binding.tvPostTime.text = date
            if (profile != "noProfile") {
                if (!isFinishing && !isDestroyed) {
                    Glide.with(this).load(profile).into(binding.ivUserAvatar)
                }
                binding.tvUserName.text = name
            } else {
                binding.tvUserName.text = "Anomyous"
            }
            if (postText == "noText") {
                binding.tvPostDescription.visibility = View.GONE
            } else {
                binding.tvPostDescription.text = postText
            }
            if (postImg == "NoImg") {
                binding.ivPostImage.visibility = View.GONE
            } else {
                if (!isFinishing && !isDestroyed) {
                    Glide.with(this).load(postImg).into(binding.ivPostImage)
                }
            }
            binding.commentsRv.layoutManager = LinearLayoutManager(this)
            val adapter = HomeAdapter(this)
            viewModel.dataList.observe(this) { comments ->
                adapter.updateList(comments)
            }
            viewModel.isLoading.observe(this){isLoading->
                if (!isLoading){
                    binding.shimmer.hideShimmer()
                    binding.shimmer.visibility = View.GONE
                    binding.commentsRv.visibility = View.VISIBLE
                }
            }
            postRepository.checkLiked(postId.toString()){
                binding.likeBtn.isChecked = it
            }
            binding.likeBtn.setOnClickListener {
                val isChecked = binding.likeBtn.isChecked
                if (isChecked){
                    val likeCount = binding.tvUpvoteCount.text.toString().toInt()
                    val reduceCount = likeCount+1


                    binding.tvUpvoteCount.text = reduceCount.toString()
                    postRepository.like(postId.toString(), path.toString(),authId.toString(),userName,postText.toString(),token.toString())

                }else{
                    val likeCount = binding.tvUpvoteCount.text.toString().toInt()
                    val reduceCount = likeCount-1

                    postRepository.removeLike(postId.toString(),path.toString())
                      binding.tvUpvoteCount.text = reduceCount.toString()
                }

            }

            binding.commentsRv.adapter = adapter
        }else{

            val path = intent.getStringExtra("path")

            seePost(path.toString()){
                if (it != null) {

                    binding.likeBtn.setOnClickListener {y->
                        val isChecked = binding.likeBtn.isChecked
                        if (isChecked){
                            val likeCount = binding.tvUpvoteCount.text.toString().toInt()
                            var reduceCount = likeCount+1


                            binding.tvUpvoteCount.text = reduceCount.toString()
                            postRepository.like(it.id.toString(), it.path.toString(),it.authId.toString(),userName,it.postText.toString(),it.token.toString())

                        }else{
                            postRepository.removeLike(it.id.toString(),it.path.toString())
                            val likeCount = binding.tvUpvoteCount.text.toString().toInt()
                            var reduceCount = likeCount-1


                            binding.tvUpvoteCount.text = reduceCount.toString()

                        }

                    }

                    postRepository.checkLiked(it.id.toString()){
                        binding.likeBtn.isChecked = it
                    }
                    binding.tvPostDescription.text = it.postText
                    viewModel.fetchDataFromDatabase(it.id.toString())
                    Glide.with(this).load(it.postImg).into(binding.ivPostImage)
                    binding.tvUpvoteCount.text = it.likes.toString()
                    binding.tvCommentCount.text = it.comment.toString()
                    if (it.isAnomyous) {
                        binding.tvUserName.text = "Anomyous"
                    } else {
                        getUserName() { profile, name ->
                            binding.tvUserName.text = name
                            binding.tvReply.setOnClickListener {c->

                                val intent = Intent(this,ReplyActivity::class.java)
                                intent.putExtra("name",name)
                                intent.putExtra("postId",it.id)

                                intent.putExtra("token",it.token)
                                startActivity(intent)

                            }
                            Glide.with(this).load(profile).into(binding.ivUserAvatar)
                        }
                    }
                    if (it.postImg == "NoImg"){
                        binding.ivPostImage.visibility = View.GONE
                    }else{
                        Glide.with(this).load(it.postImg).into(binding.ivPostImage)
                    }
                    if (it.postText.toString() == "noText") {
                       binding.tvPostDescription.visibility = View.GONE
                    } else {
                        binding.tvPostDescription.text = it.postText
                    }

                    binding.community.text = it.clubkey
                    binding.commentsRv.layoutManager = LinearLayoutManager(this)
                    val adapter = HomeAdapter(this)
                    viewModel.dataList.observe(this) { comments ->
                        adapter.updateList(comments)
                    }
                    binding.commentsRv.adapter = adapter
                    viewModel.isLoading.observe(this){isLoading->
                        if (!isLoading){
                            binding.shimmer.hideShimmer()
                            binding.shimmer.visibility = View.GONE
                            binding.commentsRv.visibility = View.VISIBLE
                        }
                    }
                }

            }

        }
    }
    private fun seePost(path:String, callback: (PostModel?) -> Unit){



        val DATABASE = Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        DATABASE.child(path).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val postData = snapshot.getValue(PostModel::class.java)
                callback(postData)



            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    private fun getUserName( callback: (name: String, fakeImg: String) -> Unit) {
        val userPath = intent.getStringExtra("userPath")
        val database = Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .reference
            .child("users").child(userPath.toString())



        database.child("name").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fakeImg = snapshot.value.toString()

                // Assuming fake name is stored in a different node or child
                val nameReference = database.child("profile")


                nameReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(nameSnapshot: DataSnapshot) {
                        val fakeName = nameSnapshot.value.toString()

                        // Callback with fake name and fake image
                        callback(fakeName, fakeImg)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle onCancelled for fake name retrieval
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled for fake image retrieval
            }
        })
    }
}