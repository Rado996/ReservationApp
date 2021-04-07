package com.bachelor.reservationapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bachelor.reservation.MessageToViewHolder
import com.bachelor.reservation.Procedure
import com.bachelor.reservation.ReservationActivity
import com.bachelor.reservation.ReservationDetailActivity
import com.bachelor.reservation.adapters.reservationAdapter
import com.bachelor.reservation.classes.Reservation
import com.bachelor.reservation.classes.Service
import com.bachelor.reservationapp.R
import com.bachelor.reservationapp.classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_messages.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.fragment_calendar.view.*
import kotlinx.android.synthetic.main.reservation_item.view.*
import kotlinx.android.synthetic.main.reservation_profileitem.view.*
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
                val intent= Intent(activity,ReservationActivity::class.java)
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

                        val rezervation = Reservation(
                                it.child("reservationID").value.toString(),
                                it.child("userID").value.toString(),
                                it.child("service").value.toString(),
                                it.child("day").value.toString(),
                                it.child("month").value.toString(),
                                it.child("year").value.toString(),
                                it.child("startHour").value.toString(),
                                it.child("startMinute").value.toString(),
                                it.child("endHour").value.toString(),
                                it.child("endMinute").value.toString(),
                        )

                        adapter.add(dayReservationsViewHolder(rezervation))
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


class dayReservationsViewHolder(val reservation: Reservation): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.resStartTime.text = reservation.startHour.plus(":").plus(reservation.startMinute)
        viewHolder.itemView.resEndTime.text = reservation.endHour.plus(":").plus(reservation.endMinute)
        viewHolder.itemView.reservedService.text = reservation.service

        val sharedPref = viewHolder.itemView.context.getSharedPreferences("Data", AppCompatActivity.MODE_PRIVATE)
        val adminID: String? = sharedPref?.getString("AdminID", "Error")

        if(FirebaseAuth.getInstance().uid == adminID) {
            viewHolder.itemView.setOnClickListener { view ->
                FirebaseDatabase.getInstance().getReference("Users").child(reservation.userID.toString()).get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user: User = it.result!!.getValue(User::class.java)!!
                        val intent = Intent(view.context, ReservationDetailActivity::class.java)
                        intent.putExtra("Reservation", reservation)
                        intent.putExtra("User", user)
                        view.context.startActivity(intent)
                    }
                }
            }
        }

        if(reservation.userID == FirebaseAuth.getInstance().uid || FirebaseAuth.getInstance().uid == adminID) {

            viewHolder.itemView.reservationEditBtn.setOnClickListener {
                val intent = Intent(it.context, ReservationActivity::class.java)
                intent.putExtra("Reservation", reservation)
                it.context.startActivity(intent)
            }

            viewHolder.itemView.reservationDeleteBtn.setOnClickListener {
                val ref = FirebaseDatabase.getInstance().getReference("Reservation").child(reservation.day.toString().plus(",").plus(reservation.month.toString()).plus(",").plus(reservation.year.toString()))
                ref.child(reservation.reservationID.toString()).removeValue()
                FirebaseFirestore.getInstance().collection("Reservations").document(reservation.reservationID.toString()).delete()
            }
        }

    }

    override fun getLayout(): Int {
        return R.layout.reservation_item
    }
}
