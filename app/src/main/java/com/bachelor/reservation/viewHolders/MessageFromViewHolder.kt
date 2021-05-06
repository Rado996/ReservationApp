package com.bachelor.reservation.viewHolders

import com.bachelor.reservationapp.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.message_from.view.*

class MessageFromViewHolder(val message: com.bachelor.reservation.classes.Message): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textFrom.text = message.text
    }

    override fun getLayout(): Int {
        return R.layout.message_from
    }
}