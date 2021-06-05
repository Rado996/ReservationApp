package com.bachelor.reservation.activities

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

        val bar: androidx.appcompat.widget.Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(bar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val service :Service = intent.getParcelableExtra("Service")
        serviceDetailTitle.text = service.title
        serviceDetailPrice.text = service.price.plus(" €")
        serviceDetailDescription.text = service.description
        serviceDetailDuration.text = service.duration
        //serviceDetailPicture.setImageURI()
        Glide.with(scrollView2).load(service.pictureLink).into(serviceDetailPicture)


    }
}