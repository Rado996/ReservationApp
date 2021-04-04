package com.bachelor.reservation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bachelor.reservation.classes.Day
import com.bachelor.reservationapp.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_reservation.*
import kotlinx.android.synthetic.main.activity_set_open_hours.*
import java.time.DayOfWeek
import java.util.*
import java.util.Calendar.MONTH

class SetOpenHours : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_open_hours)

        val c = Calendar.getInstance()

        secondDay.text =  "Pondelok"
        thirdDay.text =  "Utorok"
        forthDay.text =  "Streda"
        fifthDay.text =  "Stvrtok"
        sixthDay.text =  "Piatok"
        seventhDay.text = "Sobota"
        firstDay.text =  "Nedela"

        saveTimes.setOnClickListener {
            saveOpenTime()
        }
//         c.set(2021,0,1)
//
//        seventhDay.text = c.get(Calendar.DAY_OF_WEEK).toString()

    }

    private fun saveOpenTime() {
        var startTime = secondStartTime.text.toString().split(":")
        var endTime = secondEndTime.text.toString().split(":")
        var startHour = startTime.component1().toString()
        var startMin = startTime.component2().toString()
        var endHour = endTime.component1().toString()
        var endMin = endTime.component2().toString()

        var day = Day("2", startHour, startMin, endHour, endMin)

        val days= mutableListOf<Day>()
        days.add(day)

        startTime = thirdStartTime.text.toString().split(":")
        endTime = thirdEndTime.text.toString().split(":")
        startHour = startTime.component1().toString()
        startMin = startTime.component2().toString()
        endHour = endTime.component1().toString()
        endMin = endTime.component2().toString()

        day = Day("3", startHour, startMin, endHour, endMin)
        days.add(day)

        startTime = forthStartTime.text.toString().split(":")
        endTime = forthEndTime.text.toString().split(":")
        startHour = startTime.component1().toString()
        startMin = startTime.component2().toString()
        endHour = endTime.component1().toString()
        endMin = endTime.component2().toString()

        day = Day("4", startHour, startMin, endHour, endMin)
        days.add(day)

        startTime = firstStartTime.text.toString().split(":")
        endTime = fifthEndTime.text.toString().split(":")
        startHour = startTime.component1().toString()
        startMin = startTime.component2().toString()
        endHour = endTime.component1().toString()
        endMin = endTime.component2().toString()

        day = Day("5", startHour, startMin, endHour, endMin)
        days.add(day)

        startTime = sixthStartTime.text.toString().split(":")
        endTime = sixthEndTime.text.toString().split(":")
        startHour = startTime.component1().toString()
        startMin = startTime.component2().toString()
        endHour = endTime.component1().toString()
        endMin = endTime.component2().toString()

        day = Day("6", startHour, startMin, endHour, endMin)
        days.add(day)

        startTime = seventhStartTime.text.toString().split(":")
        endTime = seventhEndTime.text.toString().split(":")
        startHour = startTime.component1().toString()
        startMin = startTime.component2().toString()
        endHour = endTime.component1().toString()
        endMin = endTime.component2().toString()

        day = Day("7", startHour, startMin, endHour, endMin)
        days.add(day)

        startTime = firstStartTime.text.toString().split(":")
        endTime = firstEndTime.text.toString().split(":")
        startHour = startTime.component1().toString()
        startMin = startTime.component2().toString()
        endHour = endTime.component1().toString()
        endMin = endTime.component2().toString()

        day = Day("1", startHour, startMin, endHour, endMin)
        days.add(day)

        days.forEach {
            FirebaseDatabase.getInstance().getReference("OpenHour/${it.ID}").setValue(it)
        }




    }
}