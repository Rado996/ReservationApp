package com.bachelor.reservationapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bachelor.reservation.AddService
import com.bachelor.reservation.Procedure
import com.bachelor.reservation.SetOpenHours
import com.bachelor.reservation.classes.Day
import com.bachelor.reservation.classes.Service
import com.bachelor.reservation.specialHoursViewHolder
import com.bachelor.reservationapp.R
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_set_open_hours.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.service_item.view.*
import kotlinx.android.synthetic.main.special_date_item.view.*
import kotlinx.android.synthetic.main.special_date_open_hours_item.view.*


class HomeFragment : Fragment(), View.OnClickListener {
    private lateinit var viewOfLayout: View
    val servicesList = mutableListOf<Service>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        viewOfLayout = inflater!!.inflate(R.layout.fragment_home, container, false)

        viewOfLayout.addServiceBtn.setOnClickListener {
            addNewService()
        }

        viewOfLayout.setOpenTime.setOnClickListener {
            val intent= Intent(activity, SetOpenHours::class.java)
            startActivity(intent)
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

    private fun listServices() {
        val layout = viewOfLayout.linearLayoutServices
        servicesList.forEach { it ->

            //val buttonView: View = layoutInflater.inflate(R.layout.service_item,linearLayoutServices)

            val button = Button(context)
            button.id = it.id!!.toInt()
            button.text = it.title
            button.setOnClickListener { btn->
                onClick(btn)
            }

            viewOfLayout.linearLayoutServices.addView(button)
        }

        //viewOfLayout = layoutInflater!!.inflate(R.layout.fragment_home, parentFragment?.linearLayoutServices, false)
        // prerobit aby sa nacitavali len raz
    }


    private fun loadServices() {
        val ref = FirebaseDatabase.getInstance().getReference("Services")
        ref.get().addOnCompleteListener {
            if(it.isSuccessful) {
                val services = it
                val result = services.result
                val kids = result?.children
                servicesList.clear()
                kids?.forEach {

                    val service = Service(
                            it.child("id").value.toString(),
                            it.child("title").value.toString(),
                            it.child("pictureLink").value.toString(),
                            it.child("description").value.toString(),
                            it.child("duration").value.toString(),
                    )
                    servicesList.add(service)
                }
                listServices()
            } else{
                Toast.makeText(context, "Nepodarilo sa načítať dáta.", Toast.LENGTH_SHORT).show()
            }
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

class specialHourViewHolder(val datep: String, val startHours: String, val endHours:String): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val date = datep.split(',')
        viewHolder.itemView.specialDateTextView.text = date.component1().plus(".").plus(date.component2()).plus(".").plus(date.component3())
        if(endHours == "") {
            viewHolder.itemView.specialTimeTextView.text = startHours
        }else {
            viewHolder.itemView.specialTimeTextView.text = startHours.plus(" - ").plus(endHours)
        }
    }

    override fun getLayout() = R.layout.special_date_item

}