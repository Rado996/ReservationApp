package com.bachelor.reservation.viewHolders

import com.bachelor.reservationapp.R
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.special_date_open_hours_item.view.*

class specialHoursViewHolder(val datep: String, val startHours: String, val endHours:String): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val date = datep.split(',')
        viewHolder.itemView.specialDate.text = date.component1().plus(".").plus(date.component2()).plus(".").plus(date.component3())
        viewHolder.itemView.specialStart.text = startHours
        viewHolder.itemView.specialEnd.text = endHours

        viewHolder.itemView.specialHoursDeleteBtn.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("SpecialOpenHours/${datep}").removeValue()

            //viewHolder.itemView.visibility = "gone".toInt()
        }


    }

    override fun getLayout(): Int {
        return R.layout.special_date_open_hours_item
    }
}