package com.example.udemyTwitter.listeners

import com.example.udemyTwitter.util.Tweet

interface TweetListener {

fun onLayoutClick(tweet: Tweet?)
fun onLike(tweet: Tweet?)
fun onRetweet(tweet: Tweet?)


}