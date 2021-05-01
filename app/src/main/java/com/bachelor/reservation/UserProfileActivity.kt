package com.bachelor.reservation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.bachelor.reservation.classes.Reservation
import com.bachelor.reservationapp.R
import com.bachelor.reservationapp.classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
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
        }else{
            query = res.whereEqualTo("userID",FirebaseAuth.getInstance().uid)
        }

        loadUserData()

        val adapter = GroupAdapter<GroupieViewHolder>()
        query.get().addOnCompleteListener{ document->
            if(document.isSuccessful) {
                for(its in document.getResult()!!){
                    val data = its.data
                    val reservation: Reservation = Reservation(
                                                        data["reservationID"].toString(),
                                                        data["userID"].toString(),
                                                        data["service"].toString(),
                                                        data["day"].toString(),
                                                        data["month"].toString(),
                                                        data["year"].toString(),
                                                        data["startHour"].toString(),
                                                        data["startMinute"].toString(),
                                                        data["endHour"].toString(),
                                                        data["endMinute"].toString(),
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
            FirebaseDatabase.getInstance().getReference("Users").child(user.uid.toString()).get()
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


class userReservationsViewHolder(val reservation: Reservation): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val date = reservation.day.plus(".").plus(reservation.month.toString()).plus(".").plus(reservation.year.toString())
        viewHolder.itemView.profileReserationDate.text = date
        viewHolder.itemView.profileReserationTime.text = reservation.startHour.plus(":").plus(reservation.startMinute)
        viewHolder.itemView.profileReserationServices.text = reservation.service



    }

    override fun getLayout(): Int {
        return R.layout.reservation_profileitem
    }
}

