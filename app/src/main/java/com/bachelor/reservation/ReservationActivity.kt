package com.bachelor.reservation

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bachelor.reservation.classes.Day
import com.bachelor.reservation.classes.Reservation
import com.bachelor.reservation.fragments.DatePickerFragment
import com.bachelor.reservation.fragments.TimePickerFragment
import com.bachelor.reservationapp.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_reservation.*
import kotlinx.android.synthetic.main.reservation_item.*
import java.util.*


class ReservationActivity : AppCompatActivity() {


    var day = ""
    var month = ""
    var year = ""
    var startHour = ""
    var startMinute = ""
    var endHour = ""
    var endMinute = ""
    lateinit var dialogBuilder: AlertDialog.Builder
    val selectedServices = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        if(intent.hasExtra("Reservation")){
            setData(intent.getParcelableExtra<Reservation>("Reservation"))
        }

        service.setText("Zvoľte službu.")

        dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Vyberte službu.")

        setTextChangeListeners()

        SubmitResBtn.setOnClickListener {
            submitReservation()
        }

        service.setOnClickListener {
            showServicePickerDialog()
        }

    }

    private fun showServicePickerDialog() {

        FirebaseDatabase.getInstance().getReference("Services").get().addOnCompleteListener {
            if(it.isSuccessful){
                val services = Array<String>(it.result!!.childrenCount.toInt()) { i -> ""}
                var i = 0
                it.result!!.children.forEach {
                    //services.set(services.size,it.key.toString())
                    services[i]= it.key.toString()
                    i++
                }

                dialogBuilder.setMultiChoiceItems(services, null, DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                    if (isChecked)
                        selectedServices.add(services[which])
                    else if (selectedServices.contains(services[which]))
                        selectedServices.remove(services[which])
                })
                dialogBuilder.setPositiveButton("Hotovo", DialogInterface.OnClickListener { dialog, which ->
                    var Services = ""

                    for (i in 0..selectedServices.size) {
                        if(i < selectedServices.size) {
                            Services += selectedServices[i]
                            if (i < selectedServices.size - 1)
                                Services += ","
                        }
                    }
                    service.setText(Services)
                })
                dialogBuilder.setNegativeButton("Zruš", null)

                //dialogBuilder.show()
                val dialog: AlertDialog = dialogBuilder.create()
                dialog.show()
            }
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
        val userID = FirebaseAuth.getInstance().uid
        val service = service.text.toString()
        val note = userNote.text.toString()
        if(userID.isNullOrBlank()){
            Toast.makeText(this@ReservationActivity, "You have to be logged in to make reservation", Toast.LENGTH_SHORT).show()
        }else{
            val date =day.plus(",").plus(month).plus(",").plus(year)
            val ref = FirebaseDatabase.getInstance().getReference("Reservation").child(date)
            ref.get().addOnCompleteListener { reservations->
//                FirebaseDatabase.getInstance().getReference("Services").child(service).get().addOnCompleteListener { dur->
                FirebaseDatabase.getInstance().getReference("Services").get().addOnCompleteListener { services->
//                    val kid = services.result!!.child("duration")
                    var duration = 0
                    services.result!!.children.forEach {
                        if(selectedServices.contains(it.key))
                            duration += it.child("duration").value.toString().toInt()
                    }


                    val calendar = Calendar.getInstance()
                    calendar.set(year.toInt(),month.toInt()-1,day.toInt())
                    val dayInWeek = calendar.get(Calendar.DAY_OF_WEEK).toString()

                    FirebaseDatabase.getInstance().getReference("OpenHours/${dayInWeek}").get().addOnCompleteListener {
                        if (it.isSuccessful) {
                            val dayHours = it.getResult()!!.getValue(Day::class.java)

                            val available: Boolean = checkTimeAvailability(reservations, duration, dayHours)
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
                                        FirebaseFirestore.getInstance().collection("Reservations").document(it1).set(reservation)


                                    }
                                }
                            }else{

                            }

                        }
                    }



                }

            }
        }
    }

    private fun checkTimeAvailability(it: Task<DataSnapshot>, duration: Int, dayHours: Day?): Boolean {
        val newHourStart = startHour.toInt()*100
        val newMinuteStart = startMinute.toInt()

        var finMin: Int = startMinute.toInt() + duration
        var finHour: Int = startHour.toInt()

        if(finMin >= 60) {
            finHour += finMin/60
            finMin %= 60
        }
        endMinute = finMin.toString()
        endHour = finHour.toString()
        val newTimeStart = newHourStart + newMinuteStart
        val newTimeEnd = (finHour*100) + finMin


        val choosenDate = Calendar.getInstance()
        choosenDate.set(year.toInt(), month.toInt()-1, day.toInt(),startHour.toInt(),startMinute.toInt())
        val currentDate = Calendar.getInstance()

        if(choosenDate.time <= currentDate.time)
        {
            Toast.makeText(this, "Prepáčte ale zvolili ste čas v minulosti.", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        else {
            currentDate.add(3,1)
            if(choosenDate.time < (currentDate.time))
            {
                Toast.makeText(this, "Prepáčte ale rezerváciu musíte vytvoriť minimálne hodinu dopredu.", Toast.LENGTH_SHORT)
                    .show()
                return false

            }
            else {
                val dayEndTime = (dayHours!!.endHour!!.toInt() * 100) + dayHours.endMinute!!.toInt()
                val dayStartTime =
                    (dayHours!!.startHour!!.toInt() * 100) + dayHours.startMinute!!.toInt()
                if (newTimeStart < dayStartTime || newTimeEnd > dayEndTime) {
                    Toast.makeText(
                        this,
                        "Pokúšate sa vytvoriť rezerváciu v čase keď je prevádzka zatvorená.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                } else {
                    it.result?.children?.forEach {
                        val reservedHourStart = it.child("startHour").value.toString().toInt() * 100
                        val reservedMinuteStart = it.child("startMinute").value.toString().toInt()
                        val reservedHourEnding = it.child("endHour").value.toString().toInt() * 100
                        val reservedMinuteEnding = it.child("endMinute").value.toString().toInt()
                        val reservedTimeStart = reservedHourStart + reservedMinuteStart
                        val reservedTimeEnd = reservedHourEnding + reservedMinuteEnding

                        if (newTimeStart in reservedTimeStart..reservedTimeEnd || newTimeEnd in reservedTimeStart..reservedTimeEnd) {
                            Toast.makeText(
                                this,
                                "Bohužiaľ termín je už zabratý.",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            return false
                        }
                    }
                    return true
                }
            }
        }
        return false
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

