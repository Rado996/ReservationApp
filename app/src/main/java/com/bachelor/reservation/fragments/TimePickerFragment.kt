package com.bachelor.reservation.fragments

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_reservation.*
import java.util.*

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int){
        //check awailability
        var hour_s = ""
        var minute_s = ""
        if(hourOfDay < 10 )
            hour_s = "0".plus(hourOfDay.toString())
        else
            hour_s = hourOfDay.toString()

        if(minute < 10 )
            minute_s = "0".plus(minute.toString())
        else
            minute_s = minute.toString()

        activity?.choosenTime?.text = hour_s.plus(":").plus(minute_s)

    }
}