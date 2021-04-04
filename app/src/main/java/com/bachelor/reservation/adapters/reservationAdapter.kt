package com.bachelor.reservation.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bachelor.reservation.Procedure
import com.bachelor.reservation.ReservationActivity
import com.bachelor.reservation.classes.Reservation
import com.bachelor.reservation.classes.Service
import com.bachelor.reservationapp.R
import com.bachelor.reservationapp.fragments.CalendarFragment
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_reservation.view.*
import kotlinx.android.synthetic.main.gallery_item.view.*
import kotlinx.android.synthetic.main.reservation_item.view.*

class reservationAdapter (private val itemList: List<Reservation>) : RecyclerView.Adapter<reservationAdapter.reservationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): reservationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.reservation_item,   //parent je recycler view, inflatujem jeho kontext takze reservation item
                parent, false)


        return reservationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: reservationViewHolder, position: Int) {
        val currentItem = itemList[position]

        val startingTime = currentItem.startHour.plus(":").plus(currentItem.startMinute)
        val endingTime = currentItem.endHour.plus(":").plus(currentItem.endMinute)



        holder.startTimeView.text = startingTime
        holder.endTimeView.text = endingTime
        holder.serviceView.text = currentItem.service


        holder.editResView.setOnClickListener {
            val intent = Intent(it.context, ReservationActivity::class.java)
            intent.putExtra("Reservation", currentItem)
            it.context.startActivity(intent)
        }

        holder.deleteResView.setOnClickListener {
            val ref = FirebaseDatabase.getInstance().getReference("Reservation").child(currentItem.day.toString().plus(",").plus(currentItem.month.toString()).plus(",").plus(currentItem.year.toString()))
            ref.child(currentItem.reservationID.toString()).removeValue()
        }


    }

    override fun getItemCount() = itemList.size

    class reservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {     //trieda obsahuje 1 rezervaciu
        val startTimeView: TextView = itemView.resStartTime
        val endTimeView: TextView = itemView.resEndTime
        val serviceView: TextView = itemView.reservedService
        val editResView: TextView = itemView.reservationEditBtn
        val deleteResView: TextView = itemView.reservationDeleteBtn

    }

}
