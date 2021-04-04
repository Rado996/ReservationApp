package com.bachelor.reservation.fragments

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_reservation.*
import java.util.*

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {


    var Ahour: Int = 0
    var Aminute: Int = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        Ahour = c.get(Calendar.HOUR_OF_DAY)
        Aminute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, this, Ahour, Aminute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {

        if (hourOfDay < Ahour || minute < Aminute) {
            Toast.makeText(context, "Ľutujeme ale nemáme stroj času.", Toast.LENGTH_SHORT).show()
        } else {
            var hour_s = ""
            var minute_s = ""
            if (hourOfDay < 10)
                hour_s = "0".plus(hourOfDay.toString())
            else
                hour_s = hourOfDay.toString()

            if (minute < 10)
                minute_s = "0".plus(minute.toString())
            else
                minute_s = minute.toString()

            activity?.choosenTime?.text = hour_s.plus(":").plus(minute_s)

        }
    }
}