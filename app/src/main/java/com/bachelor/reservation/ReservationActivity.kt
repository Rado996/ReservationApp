package com.bachelor.reservation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.bachelor.reservation.classes.Reservation
import com.bachelor.reservation.fragments.DatePickerFragment
import com.bachelor.reservation.fragments.TimePickerFragment
import com.bachelor.reservationapp.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_reservation.*
import kotlinx.android.synthetic.main.reservation_item.*

class ReservationActivity : AppCompatActivity() {


    var day = ""
    var month = ""
    var year = ""
    var startHour = ""
    var startMinute = ""
    var endHour = ""
    var endMinute = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        if(intent.hasExtra("Reservation")){
            setData(intent.getParcelableExtra<Reservation>("Reservation"))
        }

        setTextChangeListeners()

        SubmitResBtn.setOnClickListener {
            submitReservation()
        }

    }

    private fun setData(reservation: Reservation?) {
        val hour = reservation!!.startHour
        val minute = reservation.startMinute
        val day = reservation.day
        val month = reservation.month
        val year = reservation.year

        choosenDate.text = day.plus(".").plus(month).plus(".").plus(year)
        choosenTime.text = hour.plus(":").plus(minute)
        service.setText(reservation.service)
        userNote.setText(reservation.userNote)
    }

    private fun submitReservation() {
        val userID = FirebaseAuth.getInstance().currentUser.uid
        val service = service.text.toString()
        val note = userNote.text.toString()
        if(userID.isNullOrBlank()){
            Toast.makeText(this@ReservationActivity, "You have to be logged in to make reservation", Toast.LENGTH_SHORT).show()
        }else{
            val date =day.plus(",").plus(month).plus(",").plus(year)

            val ref = FirebaseDatabase.getInstance().getReference("Reservation").child(date)
            ref.get().addOnCompleteListener {
                val available: Boolean = checkTimeAvailability(it)
                if(available){
                    if(intent.hasExtra("Reservation")){
                        val oldRes = intent.getParcelableExtra<Reservation>("Reservation")
                        if(oldRes.day == day && oldRes.month == month && oldRes.year == year){
                            FirebaseDatabase.getInstance().getReference("Reservation").child(oldRes.day.plus(",").plus(oldRes.month).plus(",").plus(oldRes.year)).removeValue()
                        }
                        val reservation = Reservation(oldRes.reservationID, userID, service, day, month, year, startHour, startMinute, endHour, endMinute, note)
                        ref.child(oldRes.reservationID.toString()).setValue(reservation)
                                .addOnSuccessListener {
                                    Toast.makeText(this@ReservationActivity, "Updated!", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this@ReservationActivity, "Failed!", Toast.LENGTH_SHORT).show()
                                }

                    }else {
                        val resID = ref.push().key
                        resID?.let { it1 ->
                            val reservation = Reservation(it1, userID, service, day, month, year, startHour, startMinute, endHour, endMinute, note)
                            ref.child(it1).setValue(reservation)
                                    .addOnSuccessListener {
                                        Toast.makeText(this@ReservationActivity, "Reserved!", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this@ReservationActivity, "Failed!", Toast.LENGTH_SHORT).show()
                                    }
                        }
                    }
                }else{
                    Toast.makeText(this@ReservationActivity, "Termin je už zabratý!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkTimeAvailability(it: Task<DataSnapshot>): Boolean {
        val newHourStart = startHour.toInt()*100
        val newMinuteStart = startMinute.toInt()

        var finMin: Int = startMinute.toInt()

        FirebaseDatabase.getInstance().getReference("Services").child(service.text.toString()).get().addOnCompleteListener {
            finMin += it.result!!.child("duration").value.toString().toInt()
        }
        Thread.sleep(1000)
        var finHour = startHour.toInt()

        if(finMin >= 60) {
            finMin -= 60
            finHour++
        }
        endMinute = finMin.toString()
        endHour = (finHour*100).toString()

        val newTimeStart = newHourStart + newMinuteStart
        val newTimeEnd = finHour + finMin

        it.result?.children?.forEach {
            val reservedHourStart = it.child("startHour").value.toString().toInt() * 100
            val reservedMinuteStart = it.child("startMinute").value.toString().toInt()
            val reservedHourEnding = it.child("endHour").value.toString().toInt() * 100
            val reservedMinuteEnding = it.child("endMinute").value.toString().toInt()
            val reservedTimeStart = reservedHourStart + reservedMinuteStart
            val reservedTimeEnd = reservedHourEnding + reservedMinuteEnding

            if (newTimeStart in reservedTimeStart..reservedTimeEnd || newTimeEnd in reservedTimeStart..reservedTimeEnd)
                return false
        }
        return true
    }

    private fun setTextChangeListeners() {
        choosenDate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                saveDate()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        choosenTime.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                saveTime()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private  fun saveDate(){
        val date = choosenDate.text.toString().split(".")
        day = date.component1()
        month = date.component2()
        year = date.component3()

    }

    private fun saveTime(){
        val time = choosenTime.text.toString().split(":")
        startHour = time.component1()
        startMinute = time.component2()
    }


    fun showTimePickerDialog(v: View) {
        val time = TimePickerFragment().show(supportFragmentManager, "timePicker")

    }

    fun showDatePickerDialog(v: View) {
        val datePicker = DatePickerFragment()
        datePicker.show(supportFragmentManager, "datePicker")
    }

}

