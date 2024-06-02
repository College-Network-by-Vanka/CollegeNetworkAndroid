package com.santhi.collegenetwork.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.businessLogic.adapter.SearchAdapter
import com.santhi.collegenetwork.businessLogic.model.User
import com.santhi.collegenetwork.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.back.setOnClickListener {
            finish()
        }
        binding.findFriendRv.layoutManager = LinearLayoutManager(this)
        //val query = Firebase.database("").reference.child("users").orderByChild("username").startAt("partialUsername").endAt("partialUsername~")
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    val start = it.toString()
                    val end = "$start~"
                    val input_query = Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("users").orderByChild("name").startAt(start).endAt(end)
                    fetchPartialUsers(input_query)

                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    val start = it.toString()
                    val end = "$start~"
                    val input_query = Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("users").orderByChild("name").startAt(start).endAt(end)
                    fetchPartialUsers(input_query)
                }
                return true
            }
        })

    }
    // Assuming you have a function that fetches partial user matches
    fun fetchPartialUsers(query: Query) {
        query.limitToFirst(10).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userList = mutableListOf<User>()
                for (userSnapshot in dataSnapshot.children) {
                    val userData = userSnapshot.getValue(User::class.java)
                    if (userData != null) {
                        userList.add(userData)
                    }
                //    Toast.makeText(this@SearchActivity, "$userData", Toast.LENGTH_SHORT).show()
                }
                // Update the adapter with the new list of users
                binding.findFriendRv.adapter = SearchAdapter(this@SearchActivity,userList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

}