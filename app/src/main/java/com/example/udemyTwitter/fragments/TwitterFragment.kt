package com.example.udemyTwitter.fragments

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.udemyTwitter.adapters.TweetListAdapter
import com.example.udemyTwitter.listeners.HomeCallback
import com.example.udemyTwitter.listeners.TweetListener
import com.example.udemyTwitter.listeners.TwitterListImpl
import com.example.udemyTwitter.util.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.RuntimeException

abstract class TwitterFragment :Fragment(){

    protected var tweetsAdapter: TweetListAdapter?= null
    protected val userId= FirebaseAuth.getInstance().currentUser?.uid
    protected var currentUser: User?= null
    protected var listener: TwitterListImpl?= null
    protected val dataBase= FirebaseFirestore.getInstance()
    protected var callback:HomeCallback?= null

    override fun onAttach(context: Context) {
        super.onAttach(context)
       if (context is HomeCallback){
           callback= context
       }else{
           throw RuntimeException(context.toString()+ "must implement HomeCallback")
       }



    }











    fun setUser(user: User?){
        this.currentUser= user
    }

    abstract fun updateList()

    override fun onResume() {
        super.onResume()
        updateList()
    }
















}