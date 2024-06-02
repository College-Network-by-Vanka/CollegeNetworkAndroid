package com.santhi.collegenetwork.businessLogic.model

import com.google.firebase.messaging.Constants.ScionAnalytics.MessageType

data class ChatHistoryModel(
    val id:String?=null,
    val recentMessage:String?=null,
    val time:String?=null,
    var unseen:Int =0,
    var type:String?= null
)
