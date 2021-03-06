package com.bachelor.reservationapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bachelor.reservation.adapters.pictureAdapter
import com.bachelor.reservation.adminActivities.PostPicture
import com.bachelor.reservationapp.R
import com.bachelor.reservationapp.classes.Picture
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

import kotlinx.android.synthetic.main.fragment_gallery.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class GalleryFragment : Fragment() {
    private lateinit var viewOfLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewOfLayout = inflater!!.inflate(R.layout.fragment_gallery, container, false)

        val sharedPref =
            viewOfLayout.context.getSharedPreferences("Data", AppCompatActivity.MODE_PRIVATE)
        val adminID: String? = sharedPref.getString("AdminID", "Error")
        if (FirebaseAuth.getInstance().uid == adminID) {
            viewOfLayout.addPicture.setVisibility(View.VISIBLE)
            viewOfLayout.addPicture.setOnClickListener {
                val intent = Intent(activity, PostPicture::class.java)
                startActivity(intent)
            }
        }
        listPictures()
        return viewOfLayout
    }


    private fun listPictures(){

        val picuresList = mutableListOf<Picture>()
        FirebaseDatabase.getInstance().getReference("Pics").get().addOnSuccessListener {

            it.children?.forEach {

                picuresList.add(
                    Picture(
                        it.key,
                        it.child("link").value.toString(),
                        it.child("description").value.toString()
                    )
                )
            }
            val pictureAdapter = pictureAdapter(picuresList)
            viewOfLayout.recycler_view_gallery_items.apply {
                adapter = pictureAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }
            .addOnFailureListener {
                Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
            }

    }
}