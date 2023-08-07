package com.example.udemyTwitter.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.udemyTwitter.R
import com.example.udemyTwitter.fragments.HomeFragment
import com.example.udemyTwitter.fragments.MyActivityFragment
import com.example.udemyTwitter.fragments.SearchFragment
import com.example.udemyTwitter.util.DATA_USERS
import com.example.udemyTwitter.util.User
import com.example.udemyTwitter.util.loadUrl
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_profile.*

class HomeActivity : AppCompatActivity() {

    private val firebaseAuth= FirebaseAuth.getInstance()


    private var sectionPagerAdapter:SectionPagerAdapter?=null
    private val homeFragment= HomeFragment()
    private val searchFragment= SearchFragment()
    private val myActivityFragment= MyActivityFragment()
    private val firebaseDB = FirebaseFirestore.getInstance()

    private var userId= FirebaseAuth.getInstance().currentUser?.uid
    private var user:User?=null

       override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_home)

           sectionPagerAdapter=SectionPagerAdapter(supportFragmentManager)


        container.adapter= sectionPagerAdapter
        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
        tabs.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
          override fun onTabSelected(tab: TabLayout.Tab?) {
             when(tab?.position){
                 0->{
                    searchBar.visibility= View.GONE
                    titleBar.text= "Home"
                    titleBar.visibility= View.VISIBLE
                 }
                 1->{
                     searchBar.visibility= View.VISIBLE
                     titleBar.visibility= View.GONE

                 }
                 2->{
                     searchBar.visibility= View.GONE
                     titleBar.text= "My Activity"
                     titleBar.visibility= View.VISIBLE


                 }
             }




           }

          override fun onTabUnselected(tab: TabLayout.Tab?) {
              TODO("Not yet implemented")
          }

          override fun onTabReselected(tab: TabLayout.Tab?) {
              TODO("Not yet implemented")
          }
      })

        logo.setOnClickListener { view ->
            startActivity(ProfileActivity.newIntent(this))
        }

        fab.setOnClickListener{  view->
            startActivity(TweetActivity.newIntent(this,userId,user?.username))
        }
        homeProgressLayout.setOnTouchListener { v, event -> true }

        search.setOnEditorActionListener { v, actionId, event ->
            if (actionId== EditorInfo.IME_ACTION_DONE || actionId== EditorInfo.IME_ACTION_SEARCH){
                SearchFragment.newHashtag(v?.text.toString())
            }
            true
        }









    }



    override fun onResume() {
        super.onResume()
        userId= FirebaseAuth.getInstance().currentUser?.uid
        if (userId==null){
          startActivity(LoginActivity.newIntent(this))
         finish()
      }

        populate()
    }

    fun populate(){
        profileProgressLayout.visibility= View.VISIBLE
        firebaseDB.collection(DATA_USERS).document(userId!!).get()
            .addOnSuccessListener {  documentSnapshot->
                homeProgressLayout.visibility= View.GONE

                 user= documentSnapshot.toObject(User::class.java)
                 user?.imageUrl.let {
                    logo.loadUrl(it,R.drawable.logo)


                }
                homeProgressLayout.visibility= View.GONE

            }

            .addOnFailureListener { e->
                e.printStackTrace()
                finish()
            }
    }



    inner class SectionPagerAdapter(fm:FragmentManager):FragmentPagerAdapter(fm){
        override fun getCount() = 3

        override fun getItem(position: Int): Fragment {
            return when(position){
                0-> homeFragment
                1-> searchFragment
                else-> myActivityFragment
            }


                    }

    }



  companion object{
      fun newIntent(context: Context)=Intent(context, HomeActivity::class.java)
  }





}