package com.bachelor.reservation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bachelor.reservation.classes.Message
import com.bachelor.reservation.classes.UserConversation
import com.bachelor.reservationapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_messages.*
import kotlinx.android.synthetic.main.message_from.view.*
import kotlinx.android.synthetic.main.message_to.view.*

class MessagesActivity : AppCompatActivity() {

    val curUserID = FirebaseAuth.getInstance().uid
    lateinit var userConversation: UserConversation
    lateinit var adminID: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)
        val sharedPref = this.getSharedPreferences("Data", AppCompatActivity.MODE_PRIVATE)
       adminID = sharedPref?.getString("AdminID", "Error").toString()
        val bar: androidx.appcompat.widget.Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(bar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userConversation = intent.getParcelableExtra("ConversationData")

        loadMessages()

        sendMessageBtn.setOnClickListener {
            sendMessage()
        }

    }

    private fun sendMessage() {
        if(userConversation.convID == ""){
            val conversationID = FirebaseDatabase.getInstance().getReference("Messsages").push().key.toString()
            var participant1ID = userConversation.participantOneID
            var participant2ID = FirebaseAuth.getInstance().uid.toString()


            if(participant2ID == adminID) {
                participant2ID = userConversation.participantOneID.toString()
                participant1ID = FirebaseAuth.getInstance().uid.toString()
            }

            FirebaseDatabase.getInstance().getReference("Users/${participant1ID}").get().addOnCompleteListener {
               if(it.isSuccessful) {
                   val participant1Name = it.result!!.child("userName").value.toString()
                   FirebaseDatabase.getInstance().getReference("Users/${participant2ID}").get().addOnCompleteListener {
                       if(it.isSuccessful){
                           val participant2Name = it.result!!.child("userName").value.toString()
                           userConversation = UserConversation(conversationID, participant1ID, participant2ID, participant1Name, participant2Name)
                            FirebaseDatabase.getInstance().getReference("Conversations/${participant2ID}").setValue(userConversation).addOnCompleteListener {
                                if(it.isSuccessful) {
                                    val msgID = FirebaseDatabase.getInstance().getReference("Messages/${conversationID}").push().key
                                    val msgText = messageTextForm.text.toString()
                                    val message: Message = Message(msgID.toString(), msgText, participant1ID.toString(), participant2ID)
                                    FirebaseDatabase.getInstance().getReference("Messages/${conversationID}/${msgID}").setValue(message).addOnCompleteListener {
                                        Toast.makeText(applicationContext, "Odoslane a vytvorena konverzacia", Toast.LENGTH_SHORT).show()
                                        messageTextForm.text.clear()
                                        loadMessages()
                                    }

                                } else {
                                    Toast.makeText(applicationContext, it.exception.toString(), Toast.LENGTH_SHORT).show()
                                }
                            }

                       }else {
                           Toast.makeText(applicationContext, it.exception.toString(), Toast.LENGTH_SHORT).show()
                       }
                   }
               } else{
                   Toast.makeText(applicationContext, it.exception.toString(), Toast.LENGTH_SHORT).show()
               }
           }
        }else{

            val msgID = FirebaseDatabase.getInstance().getReference("Messages/${userConversation.convID}").push().key
            val msgText = messageTextForm.text.toString()
            val sender = FirebaseAuth.getInstance().uid.toString()
            var receiver = ""
            if(sender == userConversation.participantOneID.toString())
                receiver = userConversation.participantTwoID.toString()
            else
                receiver = userConversation.participantOneID.toString()
            val message: Message = Message(msgID.toString(), msgText, receiver, sender)
            FirebaseDatabase.getInstance().getReference("Messages/${userConversation.convID}/${msgID}").setValue(message).addOnCompleteListener {
                Toast.makeText(applicationContext, "Odoslane", Toast.LENGTH_SHORT).show()
                messageTextForm.text.clear()
                //loadMessages()
            }
        }
    }

    private fun loadMessages() {
        val adapter = GroupAdapter<GroupieViewHolder>()
        if(userConversation.convID == "" && FirebaseAuth.getInstance().uid != adminID){
            val adapter1 = GroupAdapter<GroupieViewHolder>()

            val message = com.bachelor.reservation.classes.Message("", "Pomôžeme Vám?.", "", "")
            adapter1.add(MessageToViewHolder(message))

            messagesRecyclerView.adapter = adapter1
            messagesRecyclerView.layoutManager = LinearLayoutManager(applicationContext)

        }else{

            var convRef = FirebaseDatabase.getInstance().getReference("Messages/${userConversation.convID}")

            convRef.addChildEventListener(object: ChildEventListener
            {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {


                    val message: Message = snapshot.getValue(Message::class.java)!!
                    if(message.fromUserID == curUserID){
                        adapter.add(MessageFromViewHolder(message))
                    } else{
                        adapter.add(MessageToViewHolder(message))
                    }
                    messagesRecyclerView.adapter = adapter
                    messagesRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
                    messagesRecyclerView.scrollToPosition(adapter.itemCount -1 )
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

//            convRef.addListenerForSingleValueEvent(object: ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//
//
//                    if(snapshot.hasChildren()) {
//                        snapshot.children.forEach {
//                           val message: Message = it.getValue(Message::class.java)!!
//                            if(message.fromUserID == curUserID){
//                                adapter.add(MessageFromViewHolder(message))
//                            } else{
//                                adapter.add(MessageToViewHolder(message))
//                            }
//
//
//                        }
//                    } else{
//                        val conversation = UserConversation("", "PFMzSqH2i4auX7MvE5t4nAOtpDH3", "", "Napíšte nám.", "")
//                        adapter.add(ConversationViewHolder(conversation))
//                    }
//                    messagesRecyclerView.adapter = adapter
//                    messagesRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//            })
        }
    }
}


class MessageFromViewHolder(val message: com.bachelor.reservation.classes.Message): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textFrom.text = message.text
    }

    override fun getLayout(): Int {
        return R.layout.message_from
    }
}

class MessageToViewHolder(val message: com.bachelor.reservation.classes.Message): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textTo.text = message.text
    }

    override fun getLayout(): Int {
        return R.layout.message_to
    }
}