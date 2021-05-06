package com.bachelor.reservation.viewHolders

import com.bachelor.reservation.classes.Reservation
import com.bachelor.reservationapp.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.reservation_profileitem.view.*

class userReservationsViewHolder(val reservation: Reservation): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.profileReserationDate.text = reservation.date
        viewHolder.itemView.profileReserationTime.text = reservation.startTime
        viewHolder.itemView.profileReserationServices.text = reservation.service



    }

    override fun getLayout(): Int {
        return R.layout.reservation_profileitem
    }
}