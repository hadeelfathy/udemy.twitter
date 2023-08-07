package com.example.udemyTwitter.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.udemyTwitter.R
import com.example.udemyTwitter.adapters.TweetListAdapter
import com.example.udemyTwitter.listeners.TweetListener
import com.example.udemyTwitter.util.DATA_TWEETS
import com.example.udemyTwitter.util.DATA_TWEET_HASHTAGS
import com.example.udemyTwitter.util.Tweet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment() {

    private val dataBase= FirebaseFirestore.getInstance()
    private var currentHashtag:String= ""
    private var tweetsAdapter: TweetListAdapter?= null
    private val userId= FirebaseAuth.getInstance().currentUser?.uid
    private val listener: TweetListener?= null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tweetsAdapter= TweetListAdapter(userId!!, arrayListOf())
        tweetsAdapter?.setListener(listener)

        tweetList?.apply {
            layoutManager= LinearLayoutManager(context)
            adapter= tweetsAdapter
            addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
        }

       swipeRefresh.setOnRefreshListener {
           swipeRefresh.isRefreshing= false
           updateList()
       }
    }





    fun newHashtag(term:String){
        currentHashtag= term
        followHashtag.visibility= View.VISIBLE
        updateList()

    }


    fun updateList(){

       tweetList.visibility= View.GONE
       dataBase.collection(DATA_TWEETS).whereArrayContains(DATA_TWEET_HASHTAGS,currentHashtag).get()
           .addOnSuccessListener {  list->
               tweetList.visibility= View.VISIBLE
              val tweets= arrayListOf<Tweet>()
              for (document in list.documents){
                  val tweet= document.toObject(Tweet::class.java)
                  tweet?.let { tweets.add(it) }


              }
              val sortedList= tweets.sortedWith(compareByDescending { it.timeStamp })
               tweetsAdapter?.updateTweets(sortedList)

           }
           .addOnFailureListener { e->
               e.printStackTrace()
           }













    }









}