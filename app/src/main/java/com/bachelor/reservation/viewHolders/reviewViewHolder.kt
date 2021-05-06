package com.bachelor.reservation.viewHolders

import com.bachelor.reservation.classes.Review
import com.bachelor.reservationapp.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.review_item.view.*

class reviewViewHolder(val review: Review): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.reviewAuthorIDHidden.text = review.authorID
        viewHolder.itemView.reviewAuthor.text = review.authorName
        viewHolder.itemView.reviewText.text = review.text

    }

    override fun getLayout(): Int {
        return R.layout.review_item
    }
}