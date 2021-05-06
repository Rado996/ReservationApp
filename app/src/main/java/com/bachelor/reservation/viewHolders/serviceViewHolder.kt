package com.bachelor.reservation.viewHolders

import com.bachelor.reservation.classes.Service
import com.bachelor.reservationapp.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.service_item.view.*

class servicesViewHolder(val service: Service): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.serviceBtn.text = service.title
    }

    override fun getLayout() = R.layout.service_item

}