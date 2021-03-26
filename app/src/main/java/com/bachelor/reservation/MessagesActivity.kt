package com.bachelor.reservation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bachelor.reservation.classes.Message
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
import kotlinx.android.synthetic.main.activity_messages.*
import kotlinx.android.synthetic.main.conversation_item.view.*
import kotlinx.android.synthetic.main.message_to.view.*

class MessagesActivity : AppCompatActivity() {

    val curUserID = FirebaseAuth.getInstance().uid
    lateinit var userConversation: UserConversation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        userConversation = intent.getParcelableExtra("ConversationData")

        loadMessages()

        sendMessageBtn.setOnClickListener {
            sendMessage()
        }

    }

    private fun sendMessage() {
        if(userConversation.convID == ""){
            val iD = FirebaseDatabase.getInstance().getReference("Conversations").push().key.toString()
            val participant1ID = userConversation.participantOneID
            val participant2ID = FirebaseAuth.getInstance().uid.toString()

           FirebaseDatabase.getInstance().getReference("Users/${participant1ID}").get().addOnCompleteListener {
               if(it.isSuccessful) {
                   val participant1Name = it.result!!.child("userName").value.toString()
                   FirebaseDatabase.getInstance().getReference("Users/${participant2ID}").get().addOnCompleteListener {
                       if(it.isSuccessful){
                           val participant2Name = it.result!!.child("userName").value.toString()
                           userConversation = UserConversation(iD, participant1ID, participant2ID, participant1Name, participant2Name)
                            FirebaseDatabase.getInstance().getReference("Users/${participant1ID}/Conversations/${iD}").setValue(userConversation).addOnCompleteListener {
                                if(it.isSuccessful) {
                                    FirebaseDatabase.getInstance().getReference("Users/${participant2ID}/Conversations/${iD}").setValue(userConversation).addOnCompleteListener {
                                        if(it.isSuccessful){
                                            val msgID = FirebaseDatabase.getInstance().getReference("Conversations/${iD}").push().key
                                            val msgText = messageTextForm.text.toString()
                                            val message: Message = Message(msgID.toString(), msgText, participant1ID, participant2ID)
                                            FirebaseDatabase.getInstance().getReference("Conversations/${iD}").setValue(message).addOnCompleteListener {
                                                Toast.makeText(applicationContext, "Odoslane a vytvorena konverzacia", Toast.LENGTH_SHORT).show()
                                            }
                                        }else{
                                            Toast.makeText(applicationContext, it.exception.toString(), Toast.LENGTH_SHORT).show()
                                        }
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

            val msgID = FirebaseDatabase.getInstance().getReference("Conversations/${userConversation.convID}").push().key
            val msgText = messageTextForm.text.toString()
            val message: Message = Message(msgID.toString(), msgText, userConversation.participantOneID, userConversation.participantTwoID)
            FirebaseDatabase.getInstance().getReference("Conversations/${userConversation.convID}").setValue(message).addOnCompleteListener {
                Toast.makeText(applicationContext, "Odoslane", Toast.LENGTH_SHORT).show()
            }


        }
    }

    private fun loadMessages() {

        if(userConversation.convID == ""){
            val adapter = GroupAdapter<GroupieViewHolder>()

            val message = com.bachelor.reservation.classes.Message("", "Pomôžeme Vám?.", "", "")
            adapter.add(MessageToViewHolder(message))

            messagesRecyclerView.adapter = adapter
            messagesRecyclerView.layoutManager = LinearLayoutManager(applicationContext)

        }else{

            var convRef = FirebaseDatabase.getInstance().getReference("Conversations/${userConversation.convID}")
            convRef.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val adapter = GroupAdapter<GroupieViewHolder>()

                    if(snapshot.hasChildren()) {
                        snapshot.children.forEach {
                            val message: Message = it.getValue(Message::class.java)!!
                            if(message.fromUserID == curUserID){
                                adapter.add(MessageFromViewHolder(message))
                            } else{
                                adapter.add(MessageToViewHolder(message))
                            }


                        }
                    } else{
                        val conversation = UserConversation("", "Yaf3lLTBEEWFcNhOFnU3p7IWXaA3", "", "Napíšte nám.", "")
                        adapter.add(ConversationViewHolder(conversation))
                    }
                    messagesRecyclerView.adapter = adapter
                    messagesRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}


class MessageFromViewHolder(val message: com.bachelor.reservation.classes.Message): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.conversationUserName.text = message.text
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