package com.bachelor.reservation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bachelor.reservation.classes.Reservation
import com.bachelor.reservation.classes.UserConversation
import com.bachelor.reservationapp.R
import com.bachelor.reservationapp.classes.User
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_reservation_detail.*
import kotlinx.android.synthetic.main.reservation_item.view.*

class ReservationDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_detail)

        val bar: androidx.appcompat.widget.Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(bar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val user: User = intent.getParcelableExtra("User")
        val reservation: Reservation = intent.getParcelableExtra("Reservation")

        resDetailDate.text = reservation.day.toString().plus(".").plus(reservation.month.toString()).plus(".").plus(reservation.year.toString())
        resDetailTime.text = reservation.startHour.plus(":").plus(reservation.startMinute).plus(" - ").plus(reservation.endHour.plus(":").plus(reservation.endMinute))
        resDetailServices.text = reservation.service
        resDetailUserNote.text = reservation.userNote

        resDetailUserName.text = user.userName.plus(" ").plus(user.userSecondName)
        resDetailUserName.setOnClickListener {
            val intent = Intent(it.context,UserProfileActivity::class.java)
            intent.putExtra("User", user)
            it.context.startActivity(intent)
        }
        resDetailUserEmail.text = user.userEmail
        resDetailUserPhone.text = user.userPhone
        resDetailSendMessage.setOnClickListener {view->
            FirebaseDatabase.getInstance().getReference("Conversations").child(user.uid.toString()).get().addOnCompleteListener {
                val intent = Intent(view.context,MessagesActivity::class.java)
                lateinit var conversation: UserConversation
                if(it.isSuccessful && it.result!!.value != null){
                    conversation = it.result!!.getValue(UserConversation::class.java)!!
                }else{
                    conversation = UserConversation("",user.uid.toString(), "","" , "")
                }
                intent.putExtra("ConversationData", conversation)
                view.context.startActivity(intent)
            }
         }
    }
}