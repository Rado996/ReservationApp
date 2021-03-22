package com.bachelor.reservation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bachelor.reservation.classes.Service
import com.bachelor.reservationapp.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_procedure.*

class Procedure: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_procedure)

        val service :Service = intent.getParcelableExtra("Service")
        serviceDetailTitle.text = service.title
        serviceDetailDescription.text = service.title
        serviceDetailDuration.text = service.duration
        //serviceDetailPicture.setImageURI()
        Glide.with(scrollView2).load(service.pictureLink).into(serviceDetailPicture)

    }
}