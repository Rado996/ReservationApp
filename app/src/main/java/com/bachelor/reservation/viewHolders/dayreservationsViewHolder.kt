package com.bachelor.reservation.viewHolders

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bachelor.reservation.activities.ReservationActivity
import com.bachelor.reservation.adminActivities.ReservationDetailActivity
import com.bachelor.reservation.classes.Reservation
import com.bachelor.reservationapp.R
import com.bachelor.reservationapp.classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.reservation_item.view.*

class dayReservationsViewHolder(val reservation: Reservation): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.resStartTime.text = reservation.startTime
        viewHolder.itemView.resEndTime.text = reservation.endTime
        viewHolder.itemView.reservedService.text = reservation.service

        val sharedPref = viewHolder.itemView.context.getSharedPreferences("Data", AppCompatActivity.MODE_PRIVATE)
        val adminID: String? = sharedPref?.getString("AdminID", "Error")

        if(FirebaseAuth.getInstance().uid == adminID) {
            viewHolder.itemView.setOnClickListener { view ->
                FirebaseDatabase.getInstance().getReference("Users").child(reservation.userID.toString()).get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user: User = it.result!!.getValue(User::class.java)!!
                        val intent = Intent(view.context, ReservationDetailActivity::class.java)
                        intent.putExtra("Reservation", reservation)
                        intent.putExtra("User", user)
                        view.context.startActivity(intent)
                    }
                }
            }
        }

        if(reservation.userID == FirebaseAuth.getInstance().uid || FirebaseAuth.getInstance().uid == adminID) {

            viewHolder.itemView.reservationEditBtn.setVisibility(View.VISIBLE)
            viewHolder.itemView.reservationDeleteBtn.setVisibility(View.VISIBLE)

            viewHolder.itemView.reservationEditBtn.setOnClickListener {
                val intent = Intent(it.context, ReservationActivity::class.java)
                intent.putExtra("Reservation", reservation)
                it.context.startActivity(intent)
            }

            viewHolder.itemView.reservationDeleteBtn.setOnClickListener {
                val splitDate = reservation.date!!.split(".")

                val ref = FirebaseDatabase.getInstance().getReference("Reservation").child(splitDate.component1().plus(",").plus(splitDate.component2()).plus(",").plus(splitDate.component3()))
                ref.child(reservation.reservationID.toString()).removeValue()
                FirebaseFirestore.getInstance().collection("Reservations").document(reservation.reservationID.toString()).delete()
                Toast.makeText(it.context, "Rezervacia vymazana!", Toast.LENGTH_SHORT).show()

            }
        }

    }

    override fun getLayout(): Int {
        return R.layout.reservation_item
    }
}