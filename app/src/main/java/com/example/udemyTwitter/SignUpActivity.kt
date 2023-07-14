package com.example.udemyTwitter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.udemyTwitter.util.DATA_USERS
import com.example.udemyTwitter.util.User
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.emailLET
import kotlinx.android.synthetic.main.activity_login.emailTEL
import kotlinx.android.synthetic.main.activity_login.passwordLET
import kotlinx.android.synthetic.main.activity_login.passwordTEL
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private val firebaseAuth= FirebaseAuth.getInstance()
    private val firebaseDB= FirebaseFirestore.getInstance()

    private val firebaseAuthListener= FirebaseAuth.AuthStateListener {
        val user= firebaseAuth.currentUser?.uid
        user?.let { startActivity(HomeActivity.newIntent(this)) }
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        setTextChangedListener(emailLET,emailTEL)
        setTextChangedListener(passwordLET,passwordTEL)

        loginProgressLayout.setOnTouchListener { v, event -> true  }
    }

    fun setTextChangedListener(et: EditText, til: TextInputLayout){
        et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                til.isErrorEnabled= false

            }

            override fun afterTextChanged(s: Editable?) {
                TODO("Not yet implemented")
            }

        }

        )

    }
    fun onSignup(v: View) {
        var proceed = true


       if (userLET.text.isNullOrEmpty()){
           userTEL.error= "Username required"
           userTEL.isErrorEnabled= true
           proceed= false
       }



        if (emailLET.text.isNullOrEmpty()) {
            emailTEL.error = "Email required"
            emailTEL.isErrorEnabled = true
            proceed = false
        }

        if (passwordLET.text.isNullOrEmpty()) {
            passwordTEL.error = "password required"
            passwordTEL.isErrorEnabled = true
            proceed = false
        }

        if (proceed) {
            signupProgressLayout.visibility = View.VISIBLE
            firebaseAuth.createUserWithEmailAndPassword(
                emailLET.text.toString(),
                passwordLET.text.toString()
            )

                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        signupProgressLayout.visibility = View.GONE
                        Toast.makeText(
                            this@SignUpActivity, "Signup error:${task.exception?.localizedMessage}",
                            Toast.LENGTH_SHORT
                        )
                    }else{
                        val email:String= emailLET.text.toString()
                        val name:String= userLET.text.toString()
                        val user=User(email,name,"", arrayListOf(), arrayListOf())
                       firebaseDB.collection(DATA_USERS).document(firebaseAuth.uid!!).set(user)

                    }


                }

                .addOnFailureListener { e ->
                    e.printStackTrace()
                    signupProgressLayout.visibility = View.GONE

                }


        }
    }
        fun goTOLogin(v: View) {
            startActivity(LoginActivity.newIntent(this))
            finish()
        }

        override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(firebaseAuthListener)


            }

        override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(firebaseAuthListener)


            }

        companion object {
            fun newIntent(context: Context) = Intent(context, SignUpActivity::class.java)
        }





}