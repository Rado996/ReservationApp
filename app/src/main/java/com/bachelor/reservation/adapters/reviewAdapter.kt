package com.bachelor.reservation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bachelor.reservation.classes.Reservation
import com.bachelor.reservation.classes.Review
import com.bachelor.reservationapp.R
import kotlinx.android.synthetic.main.review_item.view.*

class reviewAdapter(private val itemList: List<Review>) : RecyclerView.Adapter<reviewAdapter.reviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): reviewViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.review_item,   //parent je recycler view, inflatujem jeho kontext takze reservation item
                parent, false)


        return reviewViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: reviewViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.reviewAuthorID.text = currentItem.authorID
        holder.reviewAuthor.text = currentItem.authorName
        holder.reviewText.text = currentItem.text



//        holder.editResView.setOnClickListener {
//            val intent = Intent(it.context, ReservationActivity::class.java)
//            intent.putExtra("Reservation", currentItem)
//            it.context.startActivity(intent)
//        }

//        holder.deleteResView.setOnClickListener {
//            val ref = FirebaseDatabase.getInstance().getReference("Reservation").child(currentItem.day.toString().plus(",").plus(currentItem.month.toString()).plus(",").plus(currentItem.year.toString()))
//            ref.child(currentItem.reservationID.toString()).removeValue()
//        }


    }

    override fun getItemCount() = itemList.size

    class reviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {     //trieda obsahuje 1 rezervaciu
        val reviewAuthor: TextView = itemView.reviewAuthor
        val reviewText: TextView = itemView.reviewText
        val reviewAuthorID: TextView = itemView.reviewAuthorIDHidden



    }

}