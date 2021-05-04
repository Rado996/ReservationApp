package com.bachelor.reservation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bachelor.reservationapp.R
import com.bachelor.reservationapp.classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()

        val bar: androidx.appcompat.widget.Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(bar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loginBtn.setOnClickListener {
            LoginButton()
        }
        registrationBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            //intent.putExtra(EXTRA_MESSAGE, text);
            startActivity(intent)
        }


    }



    fun LoginButton() {        // do something when the button is clicked
        val uEmail = findViewById<View>(R.id.editTextTextPersonName) as EditText
        val uPassword = findViewById<View>(R.id.editTextTextPassword) as EditText
        val userEmail = uEmail.text.toString().trim()
        val userPassword = uPassword.text.toString()
        FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener {
            if(it.isSuccessful){
                val userID = FirebaseAuth.getInstance().currentUser.uid
                FirebaseDatabase.getInstance().getReference("Users").child(userID).get().addOnCompleteListener {
                    if(it.isSuccessful){
                        val user: User = User(userID, it.result!!.child("userName").toString() ,it.result!!.child("userSecondName").toString(),it.result!!.child("userEmail").toString(),it.result!!.child("userPhone").toString())
                        Toast.makeText(applicationContext, "Úspešne prihlásený", Toast.LENGTH_SHORT).show()
                        val sharedPref = getSharedPreferences("Data", MODE_PRIVATE)
                        val editor = sharedPref.edit()
                        editor.putString("userName", it.result!!.child("userName").value.toString())
                        editor.apply()
                    }
                }

            }else{
                Toast.makeText(applicationContext, it.exception.toString(), Toast.LENGTH_LONG).show()
            }
        }

    }

}