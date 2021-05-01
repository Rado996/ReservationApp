package com.bachelor.reservation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bachelor.reservationapp.MainActivity
import com.bachelor.reservationapp.R
import com.bachelor.reservationapp.classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance()
    }

    fun RegisterButton(view: View?) {        // do something when the button is clicked
//        val intent = Intent(this, RegisterActivity::class.java)
//        //intent.putExtra(EXTRA_MESSAGE, text);
//        startActivity(intent)

        val userName = uName.text.toString()
        val userEmail = uEmail.text.toString()
        val userPhone = uPhone.text.toString()
        val userPassword = uPassword.text.toString()
        val userSecondName = uSecondName.text.toString()
        mAuth!!.createUserWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Success", "createUserWithEmail:success")
                    Toast.makeText(
                        this@RegisterActivity, "Register success.",
                        Toast.LENGTH_SHORT
                    ).show()
                    val user = mAuth!!.currentUser

                    val myUser = User(user.uid, userName, userSecondName, userPhone, userEmail)
                    FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().currentUser.uid)
                        .setValue(myUser).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val sharedPref = getSharedPreferences("Data", MODE_PRIVATE)
                                val editor = sharedPref.edit()
                                editor.putString("userName", userName)
                                editor.apply()
                                Toast.makeText(
                                    this@RegisterActivity, "Použivatel bol ulozeny",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@RegisterActivity, "Nepodarilo sa uzivatela registrovat",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Error", "createUserWithEmail:failure", task.exception)
                }
            }
    }

    private fun newActivity(data: String?) {
        Toast.makeText(
            this@RegisterActivity, "starting new activity",
            Toast.LENGTH_SHORT
        ).show()
        val sharedPref = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("AuthUserID", data)
        editor.apply()
        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
        //intent.putExtra(EXTRA_MESSAGE, text);
        startActivity(intent)
    }

    fun LoginButton(view: View?) {        // do something when the button is clicked
        val intent = Intent(this, LoginActivity::class.java)
        //intent.putExtra(EXTRA_MESSAGE, text);
        startActivity(intent)
    }
}