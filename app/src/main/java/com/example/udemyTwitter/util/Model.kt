package com.example.udemyTwitter.util

data class User(

    val email:String="" ,
    val username:String="" ,
    val imageUrl:String="" ,
    val followHashtags:ArrayList<String>? = arrayListOf(),
    val followUsers:ArrayList<String>? = arrayListOf()


)



data class Tweet(

    val TweetId:String?="" ,
    val userIds:ArrayList<String>? = arrayListOf(),
    val userName:String?="",
    val text:String?="",
    val imageUrl:String?="" ,
    val hashtags:ArrayList<String>? = arrayListOf(),
    val timeStamp:Long?=0 ,
    val likes:ArrayList<String>? = arrayListOf()


)










