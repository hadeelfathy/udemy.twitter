package com.example.udemyTwitter

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.play.integrity.internal.e
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
class LoginActivity : AppCompatActivity() {

    private val firebaseAuth=FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }


    fun onLogin(v:View){

        var proceed = true

        if (emailLET.text.isNullOrEmpty()){
            emailTEL.error="Email required"
            emailTEL.isErrorEnabled= true
           proceed= false
        }

        if (passwordLET.text.isNullOrEmpty()){
            passwordTEL.error="password required"
            passwordTEL.isErrorEnabled= true
            proceed= false
        }

        if (proceed){
            loginProgressLayout.visibility=View.VISIBLE
            firebaseAuth.signInWithEmailAndPassword(emailLET.text.toString(),passwordLET.text.toString())

                .addOnCompleteListener{ task->
                    if(!task.isSuccessful) {
                        loginProgressLayout.visibility= View.GONE
                        Toast.makeText(this@LoginActivity,"Login error:${task.exception?.localizedMessage}",Toast.LENGTH_SHORT)
                    }





                }

                .addOnFailureListener{e  ->
                     e.printStackTrace()
                    loginProgressLayout.visibility= View.GONE

                }




        }










    }


   fun goToSignUp(v: View){

   }






}