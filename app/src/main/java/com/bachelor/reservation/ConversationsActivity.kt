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

        var convRef = FirebaseDatabase.getInstance().getReference("Users/${userID}/Conversations")
        convRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()

                if(snapshot.hasChildren()) {
                    snapshot.children.forEach {
                        val userConvers= UserConversation(
                                it.child("convID").value.toString()
                        )
                        adapter.add(ConversationViewHolder(userConvers))
                    }
                } else{
                    val conversation = UserConversation("","Yaf3lLTBEEWFcNhOFnU3p7IWXaA3", "","Napíšte nám." , "")
                    adapter.add(ConversationViewHolder(conversation))
                }
                conversationsRecyclerView.adapter = adapter
                conversationsRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}


class ConversationViewHolder(val userConvers: UserConversation): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
//        viewHolder.itemView.conversationUserName.text = userConvers.
//        viewHolder.itemView.conversationID.text = userConvers.convID

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
