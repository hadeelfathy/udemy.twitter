package com.example.udemyTwitter.listeners

import android.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.udemyTwitter.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TwitterListImpl(val tweetList: RecyclerView, var user: User?, val callback: HomeCallback?) :
    TweetListener {

    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val DB = FirebaseFirestore.getInstance()

    override fun onLayoutClick(tweet: Tweet?) {
        tweet?.let {
            val owner = tweet.userIds?.get(0)
            if (owner != userId) {
                var followedUsers = user?.followUsers
                if (followedUsers == null) {
                    followedUsers = arrayListOf()
                }

                if (followedUsers.contains(owner) == true) {
                    AlertDialog.Builder(tweetList.context)
                        .setTitle("unfollow ${tweet.userName}?")
                        .setPositiveButton("yes") { dialog, which ->
                            tweetList.isClickable = false
                            followedUsers.remove(owner)
                            DB.collection(DATA_USERS).document(userId!!).update(
                                DATA_USER_FOLLOW, followedUsers
                            )
                                .addOnSuccessListener {

                                    tweetList.isClickable = true
                                    callback?.onUserUpdate()
                                }
                                .addOnFailureListener {
                                    tweetList.isClickable = true

                                }

                        }
                        .setNegativeButton("cancel") { dialog, which -> }
                        .show()


                } else {

                    AlertDialog.Builder(tweetList.context)
                        .setTitle("follow ${tweet.userName}?")
                        .setPositiveButton("yes") { dialog, which ->

                            tweetList.isClickable = false
                            owner?.let {
                                followedUsers.add(owner)
                                DB.collection(DATA_USERS).document(userId!!).update(
                                    DATA_USER_FOLLOW, followedUsers
                                )
                                    .addOnSuccessListener {

                                        tweetList.isClickable = true
                                        callback?.onUserUpdate()
                                    }
                                    .addOnFailureListener {
                                        tweetList.isClickable = true

                                    }
                            }
                        }
                        .setNegativeButton("cancel") { dialog, which -> }
                        .show()

                }
            }

        }


    }

    override fun onLike(tweet: Tweet?) {
        tweet?.let {
            val likes = tweet.likes
            tweetList.isClickable = false
            if (likes?.contains(userId) == true) {
                likes.remove(userId)
            } else {
                likes?.add(userId!!)
            }
            DB.collection(DATA_TWEETS).document(tweet.TweetId!!).update(DATA_TWEETS_LIKES, likes)
                .addOnSuccessListener {

                    tweetList.isClickable = true
                    callback?.onRefresh()
                }
                .addOnFailureListener {
                    tweetList.isClickable = true

                }
        }


    }

    override fun onRetweet(tweet: Tweet?) {
        tweet?.let {
            val retweets = tweet.userIds
            tweetList.isClickable = false
            if (retweets?.contains(userId) == true) {
                retweets.remove(userId)
            } else {
                retweets?.add(userId!!)
            }
            DB.collection(DATA_TWEETS).document(tweet.TweetId!!)
                .update(DATA_TWEET_USER_IDS, retweets)
                .addOnSuccessListener {
                    tweetList.isClickable = true
                    callback?.onRefresh()

                }
                .addOnFailureListener {
                    tweetList.isClickable = true

                }


        }

    }
}