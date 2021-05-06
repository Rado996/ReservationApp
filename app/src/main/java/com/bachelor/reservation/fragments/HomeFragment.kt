package com.bachelor.reservationapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bachelor.reservation.adminActivities.AddService
import com.bachelor.reservation.activities.Procedure
import com.bachelor.reservation.adminActivities.SetOpenHours
import com.bachelor.reservation.classes.Day
import com.bachelor.reservation.classes.Service
import com.bachelor.reservation.viewHolders.servicesViewHolder
import com.bachelor.reservation.viewHolders.specialHourViewHolder
import com.bachelor.reservationapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment(), View.OnClickListener {
    private lateinit var viewOfLayout: View
    val servicesList = mutableListOf<Service>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {



        viewOfLayout = inflater!!.inflate(R.layout.fragment_home, container, false)
        val sharedPref = viewOfLayout.context.getSharedPreferences("Data", AppCompatActivity.MODE_PRIVATE)
        val adminID: String? = sharedPref.getString("AdminID", "Error")
        if(FirebaseAuth.getInstance().uid == adminID){

            viewOfLayout.addServiceBtn.setVisibility(View.VISIBLE)
            viewOfLayout.setOpenTime.setVisibility(View.VISIBLE)

            viewOfLayout.addServiceBtn.setOnClickListener {
                addNewService()
            }

            viewOfLayout.setOpenTime.setOnClickListener {
                val intent= Intent(activity, SetOpenHours::class.java)
                startActivity(intent)
            }
        }



        loadOpenHours()
        setupSpecialDates()

        loadServices()

        return viewOfLayout

    }


    private fun loadOpenHours() {

        FirebaseDatabase.getInstance().getReference("OpenHours").get().addOnCompleteListener {
            if(it.isSuccessful){
                var day = it.result?.child("2")?.getValue(Day::class.java)

                if(day?.startMinute == "" && day?.endMinute == "") {
                    secondDayHours.text = day?.startHour.plus(" - ").plus(day?.endHour)
                } else if(day?.endMinute == ""){
                    secondDayHours.text = day?.startHour.plus(":").plus(day?.startMinute).plus(" - ").plus(day?.endHour)
                }  else if(day?.startMinute == ""){
                    secondDayHours.text = day?.startHour.plus(":").plus(" - ").plus(day?.endHour).plus(":").plus(day?.endMinute)
                } else{
                    secondDayHours.text = day?.startHour.plus(":").plus(day?.startMinute).plus(" - ").plus(day?.endHour).plus(":").plus(day?.endMinute)
                }


                day = it.result?.child("3")?.getValue(Day::class.java)
                if(day?.startMinute == "" && day?.endMinute == "") {
                    thirdDayHours.text = day?.startHour.plus(" - ").plus(day?.endHour)
                } else if(day?.endMinute == ""){
                    thirdDayHours.text = day?.startHour.plus(":").plus(day?.startMinute).plus(" - ").plus(day?.endHour)
                }  else if(day?.startMinute == ""){
                    thirdDayHours.text = day?.startHour.plus(":").plus(" - ").plus(day?.endHour).plus(":").plus(day?.endMinute)
                } else{
                    thirdDayHours.text = day?.startHour.plus(":").plus(day?.startMinute).plus(" - ").plus(day?.endHour).plus(":").plus(day?.endMinute)
                }

                day = it.result?.child("4")?.getValue(Day::class.java)
                if(day?.startMinute == "" && day?.endMinute == "") {
                    forthDayHours.text = day?.startHour.plus(" - ").plus(day?.endHour)
                } else if(day?.endMinute == ""){
                    forthDayHours.text = day?.startHour.plus(":").plus(day?.startMinute).plus(" - ").plus(day?.endHour)
                }  else if(day?.startMinute == ""){
                    forthDayHours.text = day?.startHour.plus(":").plus(" - ").plus(day?.endHour).plus(":").plus(day?.endMinute)
                } else{
                    forthDayHours.text = day?.startHour.plus(":").plus(day?.startMinute).plus(" - ").plus(day?.endHour).plus(":").plus(day?.endMinute)
                }

                day = it.result?.child("5")?.getValue(Day::class.java)
                if(day?.startMinute == "" && day?.endMinute == "") {
                    fifthDayHours.text = day?.startHour.plus(" - ").plus(day?.endHour)
                } else if(day?.endMinute == ""){
                    fifthDayHours.text = day?.startHour.plus(":").plus(day?.startMinute).plus(" - ").plus(day?.endHour)
                }  else if(day?.startMinute == ""){
                    fifthDayHours.text = day?.startHour.plus(":").plus(" - ").plus(day?.endHour).plus(":").plus(day?.endMinute)
                } else{
                    fifthDayHours.text = day?.startHour.plus(":").plus(day?.startMinute).plus(" - ").plus(day?.endHour).plus(":").plus(day?.endMinute)
                }

                day = it.result?.child("6")?.getValue(Day::class.java)
                if(day?.startMinute == "" && day?.endMinute == "") {
                    sixthDayHours.text = day?.startHour.plus(" - ").plus(day?.endHour)
                } else if(day?.endMinute == ""){
                    sixthDayHours.text = day?.startHour.plus(":").plus(day?.startMinute).plus(" - ").plus(day?.endHour)
                }  else if(day?.startMinute == ""){
                    sixthDayHours.text = day?.startHour.plus(":").plus(" - ").plus(day?.endHour).plus(":").plus(day?.endMinute)
                } else{
                    sixthDayHours.text = day?.startHour.plus(":").plus(day?.startMinute).plus(" - ").plus(day?.endHour).plus(":").plus(day?.endMinute)
                }

                day = it.result?.child("7")?.getValue(Day::class.java)
                if(day?.startMinute == "" && day?.endMinute == "") {
                    seventhDayHours.text = day?.startHour.plus(" - ").plus(day?.endHour)
                } else if(day?.endMinute == "" ){
                    seventhDayHours.text = day?.startHour.plus(":").plus(day?.startMinute).plus(" - ").plus(day?.endHour)
                }  else if(day?.startMinute == "" ){
                    seventhDayHours.text = day?.startHour.plus(":").plus(" - ").plus(day?.endHour).plus(":").plus(day?.endMinute)
                } else{
                    seventhDayHours.text = day?.startHour.plus(":").plus(day?.startMinute).plus(" - ").plus(day?.endHour).plus(":").plus(day?.endMinute)
                }

                day = it.result?.child("1")?.getValue(Day::class.java)
                if(day?.startMinute == "" && day?.endMinute == "") {
                    firstDayHours.text = day?.startHour.plus(" - ").plus(day?.endHour)
                } else if(day?.endMinute == ""){
                    firstDayHours.text = day?.startHour.plus(":").plus(day?.startMinute).plus(" - ").plus(day?.endHour)
                }  else if(day?.startMinute == "" ){
                    firstDayHours.text = day?.startHour.plus(":").plus(" - ").plus(day?.endHour).plus(":").plus(day?.endMinute)
                } else{
                    firstDayHours.text = day?.startHour.plus(":").plus(day?.startMinute).plus(" - ").plus(day?.endHour).plus(":").plus(day?.endMinute)
                }

            }else{
                Toast.makeText(context, "Nepodarilo sa načitať otváraciu dobu.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addNewService() {
        val intent= Intent(activity, AddService::class.java)
        intent.putExtra("serviceId",servicesList.size.toString() )
        startActivity(intent)
    }


    private fun loadServices() {    // nacitam služby z databazy, vytvorim adapter a vložim do neho služby
        val adapter = GroupAdapter<GroupieViewHolder>()
        val ref = FirebaseDatabase.getInstance().getReference("Services")
        ref.get().addOnSuccessListener {
           it.children?.forEach {
                val service = it.getValue(Service::class.java)
                adapter.add(servicesViewHolder(service!!))
            }
            servivesRecyclerView.adapter = adapter
            servivesRecyclerView.layoutManager = LinearLayoutManager(context)

        }
            .addOnFailureListener {
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
            }

    }

    override fun onClick(p0: View?) {
        val id: Int = p0?.id!!.toInt()
        val intent= Intent(activity, Procedure::class.java)
        val service: Service = servicesList[id]
        intent.putExtra("Service",service )
        startActivity(intent)
    }

    private fun setupSpecialDates() {
        val adapter = GroupAdapter<GroupieViewHolder>()
        FirebaseDatabase.getInstance().getReference("SpecialOpenHours").addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val date = snapshot.key.toString()
                val startHours = snapshot.child("startTime").value.toString()
                val endHours = snapshot.child("endTime").value.toString()

                adapter.add(specialHourViewHolder(date, startHours, endHours))

                homeSpecialOpenHoursRecycler.adapter = adapter
                homeSpecialOpenHoursRecycler.layoutManager = LinearLayoutManager(context)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }


        })
    }
}




