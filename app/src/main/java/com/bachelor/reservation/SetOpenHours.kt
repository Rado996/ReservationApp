package com.bachelor.reservation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bachelor.reservation.classes.Day
import com.bachelor.reservation.classes.Message
import com.bachelor.reservation.classes.Reservation
import com.bachelor.reservation.fragments.DatePickerFragment
import com.bachelor.reservation.fragments.TimePickerFragment
import com.bachelor.reservationapp.R
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_messages.*
import kotlinx.android.synthetic.main.activity_reservation.*
import kotlinx.android.synthetic.main.activity_set_open_hours.*
import kotlinx.android.synthetic.main.reservation_profileitem.view.*
import kotlinx.android.synthetic.main.special_date_open_hours_item.*
import kotlinx.android.synthetic.main.special_date_open_hours_item.view.*
import java.time.DayOfWeek
import java.util.*
import java.util.Calendar.MONTH
import kotlinx.android.synthetic.main.activity_reservation.choosenDate as choosenDate1

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

        loadTimes()

        saveTimes.setOnClickListener {
            saveOpenTime()
        }

        addSpecialDateOpenHours.setOnClickListener {
            addSpecialDate()
        }

        setupSpecialDates()
    }

    private fun loadTimes() {
        FirebaseDatabase.getInstance().getReference("OpenHours").get().addOnCompleteListener {
            if(it.isSuccessful){
                var day = it.result?.child("2")?.getValue(Day::class.java)
                if(day?.startMinute == "") {
                    secondStartTime.setText(day?.startHour)
                } else if(day?.endMinute == ""){
                    secondEndTime.setText(day?.endHour)
                }  else {
                    secondStartTime.setText(day?.startHour.plus(":").plus(day?.startMinute))
                    secondEndTime.setText(day?.endHour.plus(":").plus(day?.endMinute))
                }
                day = it.result?.child("3")?.getValue(Day::class.java)
                if(day?.startMinute == "") {
                    thirdStartTime.setText(day?.startHour)
                } else if(day?.endMinute == ""){
                    thirdEndTime.setText(day?.endHour)
                }  else {
                    thirdStartTime.setText(day?.startHour.plus(":").plus(day?.startMinute))
                    thirdEndTime.setText(day?.endHour.plus(":").plus(day?.endMinute))
                }

                day = it.result?.child("4")?.getValue(Day::class.java)
                if(day?.startMinute == "") {
                    forthStartTime.setText(day?.startHour)
                } else if(day?.endMinute == ""){
                    forthEndTime.setText(day?.endHour)
                }  else {
                    forthStartTime.setText(day?.startHour.plus(":").plus(day?.startMinute))
                    forthEndTime.setText(day?.endHour.plus(":").plus(day?.endMinute))
                }

                day = it.result?.child("5")?.getValue(Day::class.java)
                if(day?.startMinute == "") {
                    fifthStartTime.setText(day?.startHour)
                } else if(day?.endMinute == ""){
                    fifthEndTime.setText(day?.endHour)
                }  else {
                    fifthStartTime.setText(day?.startHour.plus(":").plus(day?.startMinute))
                    fifthEndTime.setText(day?.endHour.plus(":").plus(day?.endMinute))
                }


                day = it.result?.child("6")?.getValue(Day::class.java)
                if(day?.startMinute == "") {
                    sixthStartTime.setText(day?.startHour)
                } else if(day?.endMinute == ""){
                    sixthEndTime.setText(day?.endHour)
                }  else {
                    sixthStartTime.setText(day?.startHour.plus(":").plus(day?.startMinute))
                    sixthEndTime.setText(day?.endHour.plus(":").plus(day?.endMinute))
                }


                day = it.result?.child("7")?.getValue(Day::class.java)
                if(day?.startMinute == "") {
                    seventhStartTime.setText(day?.startHour)
                } else if(day?.endMinute == ""){
                    seventhEndTime.setText(day?.endHour)
                }  else {
                    seventhStartTime.setText(day?.startHour.plus(":").plus(day?.startMinute))
                    seventhEndTime.setText(day?.endHour.plus(":").plus(day?.endMinute))
                }


                day = it.result?.child("1")?.getValue(Day::class.java)
                if(day?.startMinute == "") {
                    firstStartTime.setText(day?.startHour)
                } else if(day?.endMinute == ""){
                    firstEndTime.setText(day?.endHour)
                }  else {
                    firstStartTime.setText(day?.startHour.plus(":").plus(day?.startMinute))
                    firstEndTime.setText(day?.endHour.plus(":").plus(day?.endMinute))
                }


            }


        }
    }

    private fun setupSpecialDates() {
        val adapter = GroupAdapter<GroupieViewHolder>()
        FirebaseDatabase.getInstance().getReference("SpecialOpenHours").addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val date = snapshot.key.toString()
                val startHours = snapshot.child("startTime").value.toString()
                val endHours = snapshot.child("endTime").value.toString()

                adapter.add(specialHoursViewHolder(date, startHours, endHours))

                specialOpenHoursForm.adapter = adapter
                specialOpenHoursForm.layoutManager = LinearLayoutManager(applicationContext)

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val date = snapshot.key.toString()
                val startHours = snapshot.child("startTime").value.toString()
                val endHours = snapshot.child("endTime").value.toString()

                adapter.add(specialHoursViewHolder(date, startHours, endHours))

                specialOpenHoursForm.adapter = adapter
                specialOpenHoursForm.layoutManager = LinearLayoutManager(applicationContext)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                setupSpecialDates()

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }

    private fun addSpecialDate() {
        val date = this.choosenDate.text.toString()
        val startTime = specialStartTime.text.toString()
        var endTime = specialEndTime.text.toString()

        if(!date.isNullOrBlank() && (!startTime.isNullOrBlank() || !endTime.isNullOrBlank())){
            val splitDate = date.split('.')
            val cdate = splitDate.component1().toString().plus(",").plus(splitDate.component2().toString()).plus(",").plus(splitDate.component3().toString())
            val hours = mutableMapOf<String, String>()
            hours["startTime"] = startTime
            hours["endTime"] = endTime
            FirebaseDatabase.getInstance().getReference("SpecialOpenHours/${cdate}").setValue(hours)

        }
        else{
            Toast.makeText(this, "Nevyplnili ste udaje.", Toast.LENGTH_SHORT).show()

        }
    }

    private fun saveOpenTime() {

        var startTime = secondStartTime.text.toString().split(":")
        var endTime = secondEndTime.text.toString().split(":")
        var startHour = ""
        var endHour = ""
        var startMin = ""
        var endMin = ""

        if(secondStartTime.text.toString().contains(':') && secondEndTime.text.toString().contains(':') ){
            startHour = startTime.component1().toString()
            startMin = startTime.component2().toString()
            endHour = endTime.component1().toString()
            endMin = endTime.component2().toString()
        }else{
            startHour = secondStartTime.text.toString()
            endHour = secondEndTime.text.toString()
            startMin = ""
            endMin = ""
        }

        var day = Day("2", startHour, startMin, endHour, endMin)

        val days= mutableListOf<Day>()
        days.add(day)

        if(thirdStartTime.text.toString().contains(':') && thirdEndTime.text.toString().contains(':') ){
            startHour = startTime.component1().toString()
            startMin = startTime.component2().toString()
            endHour = endTime.component1().toString()
            endMin = endTime.component2().toString()
        }else{
            startHour = thirdStartTime.text.toString()
            endHour = thirdEndTime.text.toString()
            startMin = ""
            endMin = ""
        }

        day = Day("3", startHour, startMin, endHour, endMin)
        days.add(day)

        if(forthStartTime.text.toString().contains(':') && forthEndTime.text.toString().contains(':') ){
            startHour = startTime.component1().toString()
            startMin = startTime.component2().toString()
            endHour = endTime.component1().toString()
            endMin = endTime.component2().toString()
        }else{
            startHour = forthStartTime.text.toString()
            endHour = forthEndTime.text.toString()
            startMin = ""
            endMin = ""
        }

        day = Day("4", startHour, startMin, endHour, endMin)
        days.add(day)

        if(fifthStartTime.text.toString().contains(':') && fifthEndTime.text.toString().contains(':') ){
            startHour = startTime.component1().toString()
            startMin = startTime.component2().toString()
            endHour = endTime.component1().toString()
            endMin = endTime.component2().toString()
        }else{
            startHour = fifthStartTime.text.toString()
            endHour = fifthEndTime.text.toString()
            startMin = ""
            endMin = ""
        }

        day = Day("5", startHour, startMin, endHour, endMin)
        days.add(day)

        if(sixthStartTime.text.toString().contains(':') && sixthEndTime.text.toString().contains(':') ){
            startHour = startTime.component1().toString()
            startMin = startTime.component2().toString()
            endHour = endTime.component1().toString()
            endMin = endTime.component2().toString()
        }else{
            startHour = sixthStartTime.text.toString()
            endHour = sixthEndTime.text.toString()
            startMin = ""
            endMin = ""
        }

        day = Day("6", startHour, startMin, endHour, endMin)
        days.add(day)

        if(seventhStartTime.text.toString().contains(':') && seventhEndTime.text.toString().contains(':') ){
            startHour = startTime.component1().toString()
            startMin = startTime.component2().toString()
            endHour = endTime.component1().toString()
            endMin = endTime.component2().toString()
        }else{
            startHour = seventhStartTime.text.toString()
            endHour = seventhEndTime.text.toString()
            startMin = ""
            endMin = ""
        }

        day = Day("7", startHour, startMin, endHour, endMin)
        days.add(day)

        if(firstStartTime.text.toString().contains(':') && firstEndTime.text.toString().contains(':') ){
            startHour = startTime.component1().toString()
            startMin = startTime.component2().toString()
            endHour = endTime.component1().toString()
            endMin = endTime.component2().toString()
        }else{
            startHour = firstStartTime.text.toString()
            endHour = firstEndTime.text.toString()
            startMin = ""
            endMin = ""
        }

        day = Day("1", startHour, startMin, endHour, endMin)
        days.add(day)

        days.forEach {
            FirebaseDatabase.getInstance().getReference("OpenHours/${it.ID}").setValue(it)
        }
    }

    fun showDatePickerDialog(v: View) {
        DatePickerFragment().show(supportFragmentManager, "datePicker")
    }
}


class specialHoursViewHolder(val datep: String, val startHours: String, val endHours:String): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val date = datep.split(',')
        viewHolder.itemView.specialDate.text = date.component1().plus(".").plus(date.component2()).plus(".").plus(date.component3())
        viewHolder.itemView.specialStart.text = startHours
        viewHolder.itemView.specialEnd.text = endHours

        viewHolder.itemView.specialHoursDeleteBtn.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("SpecialOpenHours/${datep}").removeValue()

            //viewHolder.itemView.visibility = "gone".toInt()
        }


    }

    override fun getLayout(): Int {
        return R.layout.special_date_open_hours_item
    }
}
