package com.bachelor.reservation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bachelor.reservation.classes.Conversation
import com.bachelor.reservation.classes.UserConversation
import com.bachelor.reservationapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter

import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_conversations.*
import kotlinx.android.synthetic.main.conversation_item.view.*

class ConversationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversations)

        loadConversations()
    }

    private fun loadConversations() {
       val userID = FirebaseAuth.getInstance().uid

        if(userID == "PFMzSqH2i4auX7MvE5t4nAOtpDH3"){

            var convRef = FirebaseDatabase.getInstance().getReference("Conversations")
            convRef.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val adapter = GroupAdapter<GroupieViewHolder>()

                    if(snapshot.hasChildren()) {
                    snapshot.children.forEach {
                        val userConvers= it.getValue(UserConversation::class.java)
                        adapter.add(ConversationViewHolder(userConvers!!))
                        }
                    } else{
                        val conversation = UserConversation("","PFMzSqH2i4auX7MvE5t4nAOtpDH3", "","Žiadne správy." , "")
                        adapter.add(ConversationViewHolder(conversation))
                    }
                    conversationsRecyclerView.adapter = adapter
                    conversationsRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        }else{
            var convRef = FirebaseDatabase.getInstance().getReference("Conversations/${userID}")
            convRef.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val adapter = GroupAdapter<GroupieViewHolder>()
                    val userConvers= snapshot.getValue(UserConversation::class.java)
                    adapter.add(ConversationViewHolder(userConvers!!))
                    if(snapshot.hasChildren()) {
//                    snapshot.children.forEach {
////                        val userConvers= it.getValue(UserConversation::class.java)
//                        val userConvers = UserConversation(
//                                it.child("convID").value.toString(),
//                                it.child("participantOneID").value.toString(),
//                                it.child("participantTwoID").value.toString(),
//                                it.child("participantOneName").value.toString(),
//                                it.child("participantTwoName").value.toString())
                        //}
                    } else{
                        val conversation = UserConversation("","PFMzSqH2i4auX7MvE5t4nAOtpDH3", "","Napíšte nám." , "")
                        adapter.add(ConversationViewHolder(conversation))
                    }
                    conversationsRecyclerView.adapter = adapter
                    conversationsRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }


    }
}


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
            val intent = Intent(it.context,MessagesActivity::class.java)
            intent.putExtra("ConversationData", userConvers)
            it.context.startActivity(intent)
        }

    }

    override fun getLayout(): Int {
        return R.layout.conversation_item
    }


}
