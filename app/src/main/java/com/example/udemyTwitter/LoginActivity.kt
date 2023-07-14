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
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout
import com.google.android.play.integrity.internal.e
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
class LoginActivity : AppCompatActivity() {

    private val firebaseAuth=FirebaseAuth.getInstance()

    private val firebaseAuthListener= FirebaseAuth.AuthStateListener {
        val user= firebaseAuth.currentUser?.uid
            user?.let { startActivity(HomeActivity.newIntent(this)) }
            finish()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

       setTextChangedListener(emailLET,emailTEL)
       setTextChangedListener(passwordLET,passwordTEL)

       loginProgressLayout.setOnTouchListener { v, event -> true  }

    }

    fun setTextChangedListener(et:EditText,til:TextInputLayout){
        et.addTextChangedListener(object :TextWatcher{
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
      startActivity(SignUpActivity.newIntent(this))
   }

    override fun onStart() {
        super.onStart()
      firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(firebaseAuthListener)

    }
   companion object{
       fun newIntent(context: Context)= Intent(context,LoginActivity::class.java)


   }

}