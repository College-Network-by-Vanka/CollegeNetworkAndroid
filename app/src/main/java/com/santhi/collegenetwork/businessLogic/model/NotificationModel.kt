package com.santhi.collegenetwork.businessLogic.model

data class NotificationModel(
    val notificationId:String?=null,
    val title: String?=null,
    val message: String?=null,
    val to:String?=null,
    val from:String?=null,
    val time:String?=null,
    val seen:Boolean = false,

    )