package com.bachelor.reservation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bachelor.reservationapp.R
import com.xwray.groupie.GroupAdapter

import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_conversations.*

class ConversationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversations)

        val adapter = GroupAdapter<GroupieViewHolder>()

        adapter.add(ConversationItem())
        adapter.add(ConversationItem())
        adapter.add(ConversationItem())

        conversationsRecyclerView.adapter = adapter
        conversationsRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
    }
}


class ConversationItem: Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

    override fun getLayout(): Int {
        return R.layout.conversation_item
    }


}
