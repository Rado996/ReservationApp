package com.bachelor.reservation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bachelor.reservationapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_user_profile.*

class UserDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        loadUserData(intent.getStringExtra("userID"))

    }

    private fun loadUserData(uID: String) {


        FirebaseDatabase.getInstance().getReference("Users").child(uID).get()
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.e("firebase", "Error getting data", task.exception)
                    } else {
                        Log.d("firebaseresult", task.result!!.key) //userID
                        Log.d("firebasevalue", task.result!!.value.toString() ) //array dat usera
                        Log.d("firebasechilduser",task.result!!.child("userName").value.toString()) //data userName

                        displayUserData(task.result!!)

                    }
                }
    }

    private fun displayUserData(user: DataSnapshot) {
        UPEmail.text = user.child("userEmail").value.toString()
        UPName.text = user.child("userName").value.toString()
        UPSecondName.text = user.child("userSecondName").value.toString()
        UPPhoneNumber.text = user.child("userPhone").value.toString()

    }
}