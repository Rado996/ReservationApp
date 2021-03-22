package com.bachelor.reservation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.bachelor.reservationapp.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
    }

    fun LoginButton(view: View?) {        // do something when the button is clicked

        val uEmail = findViewById<View>(R.id.editTextTextPersonName) as EditText
        val uPassword = findViewById<View>(R.id.editTextTextPassword) as EditText
        val userEmail = uEmail.text.toString()
        val userPassword = uPassword.text.toString()
        val sharedPref = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("AuthUserName", userEmail)
        editor.putString("AuthUserPassword", userPassword)
        editor.apply()

        //int duration = Toast.LENGTH_SHORT;
        //Toast toast = Toast.makeText(context, userName, duration);
        //toast.show();

        //intent.putExtra(EXTRA_MESSAGE, text);
        startActivity(parentActivityIntent)
    }


    fun RegisterButton(view: View?) {        // do something when the button is clicked
        val intent = Intent(this, RegisterActivity::class.java)
        //intent.putExtra(EXTRA_MESSAGE, text);
        startActivity(intent)
    }
}