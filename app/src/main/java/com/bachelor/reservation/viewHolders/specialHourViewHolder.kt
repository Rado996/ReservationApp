package com.bachelor.reservation.viewHolders

import com.bachelor.reservationapp.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.special_date_item.view.*

class specialHourViewHolder(val datep: String, val startHours: String, val endHours:String): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val date = datep.split(',')
        viewHolder.itemView.specialDateTextView.text = date.component1().plus(".").plus(date.component2()).plus(".").plus(date.component3())
        if(endHours == "") {
            viewHolder.itemView.specialTimeTextView.text = startHours
        }else {
            viewHolder.itemView.specialTimeTextView.text = startHours.plus(" - ").plus(endHours)
        }
    }

    override fun getLayout() = R.layout.special_date_item

}