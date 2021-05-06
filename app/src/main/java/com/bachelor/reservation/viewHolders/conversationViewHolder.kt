package com.bachelor.reservation.viewHolders

import android.content.Intent
import com.bachelor.reservation.activities.MessagesActivity
import com.bachelor.reservation.classes.UserConversation
import com.bachelor.reservationapp.R
import com.google.firebase.auth.FirebaseAuth
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.conversation_item.view.*

class ConversationViewHolder(val userConvers: UserConversation): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        if(userConvers.participantOneID == FirebaseAuth.getInstance().uid){
            viewHolder.itemView.conversationUserName.text = userConvers.participantTwoName
        } else {
            viewHolder.itemView.conversationUserName.text = userConvers.participantOneName
        }
        viewHolder.itemView.conversationID.text = userConvers.convID

        viewHolder.itemView.setOnClickListener {
            // new Mesages activity, ak bude covnersation id NULL tak vytvorim novu po odoslani spravy
            val intent = Intent(it.context, MessagesActivity::class.java)
            intent.putExtra("ConversationData", userConvers)
            it.context.startActivity(intent)
        }

    }

    override fun getLayout(): Int {
        return R.layout.conversation_item
    }


}