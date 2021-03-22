package com.bachelor.reservation.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_reservation.*
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it

        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        //check awailability
        var day_s = ""
        var month_s = ""
        if(day < 10 )
            day_s = "0".plus(day.toString())
        else
            day_s = day.toString()

        if(month < 10 )
            month_s = "0".plus((month+1).toString())
        else
            month_s = month.toString()
        activity?.choosenDate?.text = day_s.plus(".").plus(month_s).plus(".").plus(year.toString())
    }
}