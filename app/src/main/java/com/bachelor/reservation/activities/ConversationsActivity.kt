package com.bachelor.reservation.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bachelor.reservation.classes.UserConversation
import com.bachelor.reservation.viewHolders.ConversationViewHolder
import com.bachelor.reservationapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter

import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_conversations.*

class ConversationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversations)

        val bar: androidx.appcompat.widget.Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(bar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loadConversations()
    }

    private fun loadConversations() {
       val userID = FirebaseAuth.getInstance().uid

        if(userID == "H2GR3OXOFQg1gEqDHrHfxpzh2Wc2"){

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
                        val conversation = UserConversation("","H2GR3OXOFQg1gEqDHrHfxpzh2Wc2", "","Žiadne správy." , "")
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
                    if(snapshot.hasChildren()) {
                    val userConvers= snapshot.getValue(UserConversation::class.java)
                    adapter.add(ConversationViewHolder(userConvers!!))

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
                        val conversation = UserConversation("","H2GR3OXOFQg1gEqDHrHfxpzh2Wc2", "","Napíšte nám." , "")
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



