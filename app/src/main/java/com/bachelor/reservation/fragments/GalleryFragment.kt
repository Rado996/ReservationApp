package com.bachelor.reservationapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bachelor.reservationapp.PostPicture
import com.bachelor.reservationapp.R

import kotlinx.android.synthetic.main.fragment_gallery.view.*


class GalleryFragment : Fragment() {
    private lateinit var viewOfLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewOfLayout = inflater!!.inflate(R.layout.fragment_gallery, container, false)
        viewOfLayout.addPicture.setOnClickListener {
            val intent= Intent(activity, PostPicture::class.java)
            startActivity(intent)
        }



        return viewOfLayout
    }


}