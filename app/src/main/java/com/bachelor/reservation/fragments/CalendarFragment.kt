package com.bachelor.reservationapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bachelor.reservation.activities.ReservationActivity
import com.bachelor.reservation.classes.Reservation
import com.bachelor.reservation.viewHolders.dayReservationsViewHolder
import com.bachelor.reservationapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.fragment_calendar.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class CalendarFragment : Fragment() {
    private lateinit var viewOfLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        viewOfLayout = inflater!!.inflate(R.layout.fragment_calendar, container, false)

        viewOfLayout.createReservationBtn.setOnClickListener {
            if(FirebaseAuth.getInstance().currentUser != null){
                val intent= Intent(activity, ReservationActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(context, "Najpr sa prihláste pre vytvorenie rezervácie!", Toast.LENGTH_SHORT).show()
            }

        }

        val calenda: CalendarView = viewOfLayout.findViewById(R.id.calendarViewReservations)

        calenda.setOnDateChangeListener(CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->
            var Smonth = (month+1).toString()
            if(month+1<10)
                Smonth = "0".plus((month+1).toString())
            var Sday = dayOfMonth.toString()
            if(dayOfMonth<10)
                Sday = "0".plus(dayOfMonth.toString())

            val selectedDate = Sday.toString().plus(",").plus(Smonth).plus(",").plus(year.toString())
            listReservations(selectedDate)
        })


        return viewOfLayout
    }

    private  fun listReservations(selectedDate: String) = CoroutineScope(Dispatchers.IO).launch {
        try {

            val adapter = GroupAdapter<GroupieViewHolder>()
            val ref = FirebaseDatabase.getInstance().getReference("Reservation").child(selectedDate)
            ref.get().addOnCompleteListener {
                if(it.isSuccessful) {
                    val reservation = it
                    val result = reservation.result
                    val kids = result?.children
                    kids?.forEach {

                        val rezervation = it.getValue(Reservation::class.java)

                        adapter.add(dayReservationsViewHolder(rezervation!!))
                        //reservationList.add(Picture(it.key,it.child("link").value.toString(),it.child("description").value.toString()))
                    }
                    recyclerViewReservationItems.adapter = adapter
                    recyclerViewReservationItems.layoutManager = LinearLayoutManager(context)
                }else{
                    Toast.makeText(context, it.exception.toString(), Toast.LENGTH_LONG).show()
                }
            }

        } catch (e : Exception){
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

}



