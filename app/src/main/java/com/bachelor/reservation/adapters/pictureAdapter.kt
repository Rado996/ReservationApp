package com.bachelor.reservation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bachelor.reservationapp.R
import com.bachelor.reservationapp.classes.Picture
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.gallery_item.view.*

class pictureAdapter(private val itemList: List<Picture>) : RecyclerView.Adapter<pictureAdapter.pictureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): pictureViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item,   //parent je recycler view, inflatujem jeho kontext takze gallery item
                parent, false)

        return pictureViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: pictureViewHolder, position: Int) {
        val currentItem = itemList[position]
        //holder.imageView.setImageURI(currentItem.link)
        Glide.with(holder.itemView).load(currentItem.link).into(holder.imageView)
        holder.textView.text = currentItem.description
    }

    override fun getItemCount() = itemList.size

    class pictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {     //trieda obsahuje 1 prvok galerie
        val imageView: ImageView = itemView.post_picture
        val textView: TextView = itemView.picture_description
    }

}