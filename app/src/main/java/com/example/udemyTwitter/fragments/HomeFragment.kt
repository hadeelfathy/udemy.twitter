package com.example.udemyTwitter.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.udemyTwitter.R
import com.example.udemyTwitter.adapters.TweetListAdapter
import com.example.udemyTwitter.listeners.TwitterListImpl
import com.example.udemyTwitter.util.DATA_TWEETS
import com.example.udemyTwitter.util.DATA_TWEET_HASHTAGS
import com.example.udemyTwitter.util.DATA_TWEET_USER_IDS
import com.example.udemyTwitter.util.Tweet
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.swipeRefresh
import kotlinx.android.synthetic.main.fragment_search.tweetList

class HomeFragment : TwitterFragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        listener= TwitterListImpl(tweetList,currentUser,callback)
        tweetsAdapter= TweetListAdapter(userId!!, arrayListOf())
        tweetsAdapter?.setListener(listener)

        tweetList?.apply {
            layoutManager= LinearLayoutManager(context)
            adapter= tweetsAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing= false
            updateList()
        }


    }


    override fun updateList() {
        tweetList.visibility= View.GONE
        val tweets= arrayListOf<Tweet>()

       currentUser?.let {
           for (hashtag in it.followHashtags!!){
               dataBase.collection(DATA_TWEETS).whereArrayContains(DATA_TWEET_HASHTAGS,hashtag).get()
                   .addOnSuccessListener { list->
                       for (document in list.documents ){
                           val tweet= document.toObject(Tweet::class.java)
                           tweet?.let { tweets.add(it) }
                           updateAdapter(tweets)
                       }

                   }
                   .addOnFailureListener { e->
                       e.printStackTrace()
                       tweetList.visibility= View.VISIBLE

                   }
           }
           for (user in it.followUsers!!){
               dataBase.collection(DATA_TWEETS).whereArrayContains(DATA_TWEET_USER_IDS,user).get()
                   .addOnSuccessListener { list->
                       for (document in list.documents ){
                           val tweet= document.toObject(Tweet::class.java)
                           tweet?.let { tweets.add(it) }
                           updateAdapter(tweets)
                       }

                   }
                   .addOnFailureListener { e->
                       e.printStackTrace()
                       tweetList.visibility= View.VISIBLE

                   }


           }
       }





    }

    private fun updateAdapter(tweets:List<Tweet>){
        val sortedList= tweets.sortedWith(compareByDescending { it.timeStamp })
        tweetsAdapter?.updateTweets(removeDuplicated(sortedList))

    }
    private fun removeDuplicated(originalList:List<Tweet>)= originalList.distinctBy { it.TweetId }























}