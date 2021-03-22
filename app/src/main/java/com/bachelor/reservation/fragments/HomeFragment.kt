package com.bachelor.reservationapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.bachelor.reservation.AddService
import com.bachelor.reservation.Procedure
import com.bachelor.reservation.classes.Service
import com.bachelor.reservationapp.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.service_item.view.*


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

        loadServices()

        return viewOfLayout

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
        }

    }

    override fun onClick(p0: View?) {
        val id: Int = p0?.id!!.toInt()
        val intent= Intent(activity, Procedure::class.java)
        val service: Service = servicesList[id]
        intent.putExtra("Service",service )
        startActivity(intent)
    }

}