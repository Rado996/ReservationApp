package com.bachelor.reservation.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_reservation.*
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    var Ayear: Int = 0
    var Amonth: Int = 0
    var Aday: Int = 0
    lateinit var c: Calendar

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        c = Calendar.getInstance()
        Ayear = c.get(Calendar.YEAR)
        Amonth = c.get(Calendar.MONTH)
        Aday = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it

        return DatePickerDialog(requireActivity(), this, Ayear, Amonth, Aday)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        //check awailability

        val calendar = Calendar.getInstance()
        calendar.set(year,month,day)
        if(calendar < c){
            Toast.makeText(context, "Ľutujeme ale nemáme stroj času.", Toast.LENGTH_SHORT).show()
        }else {
            var day_s = ""
            var month_s = ""
            if (day < 10)
                day_s = "0".plus(day.toString())
            else
                day_s = day.toString()

            if (month < 10)
                month_s = "0".plus((month + 1).toString())
            else
                month_s = month.toString()
            activity?.choosenDate?.text = day_s.plus(".").plus(month_s).plus(".").plus(year.toString())
        }
    }
}