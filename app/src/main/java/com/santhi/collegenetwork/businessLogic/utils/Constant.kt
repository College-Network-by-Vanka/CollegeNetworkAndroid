package com.santhi.collegenetwork.businessLogic.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object Constant {
    private val database = Firebase.database("https://college-network-f83f1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("clubs")

}