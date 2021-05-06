package com.bachelor.reservation.activities

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setup()

    }

    private fun setup() {   //nastavenie tlačitdiel
        val bar: androidx.appcompat.widget.Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(bar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loginBtn.setOnClickListener {
            LoginButton()
        }
        registrationBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }


    fun LoginButton() {        // Prihlásenie
        val uEmail = findViewById<View>(R.id.editTextTextPersonName) as EditText
        val uPassword = findViewById<View>(R.id.editTextTextPassword) as EditText
        val userEmail = uEmail.text.toString().trim()
        val userPassword = uPassword.text.toString()
        FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail, userPassword)
            .addOnSuccessListener {
                val userID = it.user.uid
                FirebaseDatabase.getInstance().getReference("Users").child(userID).get()
                    .addOnSuccessListener {
                        //načitanie používateskeho mena

                        val user: User = it.getValue(User::class.java)!!
                        Toast.makeText(applicationContext, "Úspešne prihlásený", Toast.LENGTH_SHORT)
                            .show()
                        val sharedPref = getSharedPreferences("Data", MODE_PRIVATE)
                        val editor = sharedPref.edit()
                        editor.putString("userName", user.userName)
                        editor.apply()
                    }
                    .addOnFailureListener {
                        Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_LONG).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_LONG).show()
            }



    }

}