package com.bachelor.reservation.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.bachelor.reservation.classes.Reservation
import com.bachelor.reservation.viewHolders.userReservationsViewHolder
import com.bachelor.reservationapp.R
import com.bachelor.reservationapp.classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_messages.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.message_from.view.*
import kotlinx.android.synthetic.main.message_to.view.*
import kotlinx.android.synthetic.main.reservation_profileitem.view.*

class UserProfileActivity : AppCompatActivity() {
    lateinit var user: User
    lateinit var query: Query

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val bar: androidx.appcompat.widget.Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(bar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val res = FirebaseFirestore.getInstance().collection("Reservations")

        if(intent.hasExtra("User")){
            user = intent.getParcelableExtra("User")
            query = res.whereEqualTo("userID",user.uid)
            displayUserData(user)
        }else{
            query = res.whereEqualTo("userID",FirebaseAuth.getInstance().uid)
            loadUserData()
        }

        val adapter = GroupAdapter<GroupieViewHolder>()
        query.get().addOnCompleteListener{ document->
            if(document.isSuccessful) {
                for(its in document.getResult()!!){
                    val data = its.data
                    val reservation: Reservation = Reservation(
                                                        data["reservationID"].toString(),
                                                        data["userID"].toString(),
                                                        data["service"].toString(),
                                                        data["date"].toString(),
                                                        data["startTime"].toString(),
                                                        data["endTime"].toString(),
                                                        data["userNote"].toString(),
                                                        data["confirmed"] as Boolean?,
                                                        data["finished"] as Boolean?,
                                                        data["adminNote"].toString())
                    adapter.add(userReservationsViewHolder(reservation))
                }
                userReservationsHistory.adapter = adapter
                userReservationsHistory.layoutManager = LinearLayoutManager(applicationContext)
            }
        }
    }

    private fun loadUserData() {

        if(intent.hasExtra("User")) {
            displayUserData(user)

        }else {
            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().uid.toString()).get()
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.e("firebase", "Error getting data", task.exception)
                    } else {

                        displayUserData(task.result!!.getValue(User::class.java)!!)

                    }
                }
        }
    }

    private fun displayUserData(user: User) {
        UPEmail.text = user.userEmail
        UPName.text = user.userName.plus(" ").plus(user.userSecondName)
        UPPhoneNumber.text = user.userPhone

    }

}




