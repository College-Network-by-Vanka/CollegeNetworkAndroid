package com.santhi.collegenetwork.businessLogic.model

data class PostModel (
    val postText:String?=null,
    val postImg:String?=null,
    val likes:Int=0,
    val comment:Int=0,
    val time:String?=null,
    val id:String?=null,
    val authId:String?=null,
    val token:String?=null,
    val path:String?=null,
    val clubkey:String?=null,
    var isAnomyous:Boolean = false
)