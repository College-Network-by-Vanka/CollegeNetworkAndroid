package com.santhi.collegenetwork.businessLogic.model

import java.util.UUID

data class ChatModel(
    val message:String?=null,
    val sendMessageTime:String?=null,
    val senderId:String?=null,
    val reciverId:String?=null,
    var seen:Boolean = false,
    var id:String?=null,


)
