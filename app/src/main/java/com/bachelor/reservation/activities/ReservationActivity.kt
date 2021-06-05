package com.bachelor.reservation.activities

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_service.*
import kotlinx.android.synthetic.main.activity_reservation.*
import kotlinx.android.synthetic.main.reservation_item.*
import java.util.*


class ReservationActivity : AppCompatActivity() {


    val SAVED_DATE_BUNDLE_KEY = "RES_DATE"
    val SAVED_TIME_BUNDLE_KEY = "RES_TIME"
    val SAVED_NOTE_BUNDLE_KEY = "RES_NOTE"


    var day = ""
    var month = ""
    var year = ""
    var startHour = ""
    var startMinute = ""
    var endHour = ""
    var endMinute = ""

    var date = ""
    var startTime = ""
    var endTime = ""
    var specialDayStartTime = "null"
    var specialDayEndTime = "null"

    var duration = 0

    lateinit var dialogBuilder: AlertDialog.Builder
    val selectedServices = mutableListOf<String>()
    lateinit var services: Array<String>
    lateinit var checktServices: BooleanArray

    lateinit var dayReservations: DataSnapshot
    lateinit var weekDay: Day


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        val bar: androidx.appcompat.widget.Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(bar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loadServices()

        if(intent.hasExtra("Reservation")){
            setData(intent.getParcelableExtra<Reservation>("Reservation"))
        }

//        chooosenService.setText("Zvoľte službu.")

        dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Vyberte službu.")

        setTextChangeListeners()

        SubmitResBtn.setOnClickListener {
            submitReservation()
        }

        picsServices.setOnClickListener {
            showServicePickerDialog()
        }

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {   // ak mam uložene dáta tak ich ziskam z bundlu
        super.onRestoreInstanceState(savedInstanceState)


        choosenTime.setText(savedInstanceState.getString(SAVED_DATE_BUNDLE_KEY))
        choosenDate.setText(savedInstanceState.getString(SAVED_TIME_BUNDLE_KEY))
        userNote.setText(savedInstanceState.getString(SAVED_NOTE_BUNDLE_KEY))
    }

    override fun onSaveInstanceState(outState: Bundle) {        // uložím dáta ak sa ide aktivita ukončiť
        super.onSaveInstanceState(outState)

        outState.putString(SAVED_DATE_BUNDLE_KEY, date)
        outState.putString(SAVED_TIME_BUNDLE_KEY, startTime)
        outState.putString(SAVED_NOTE_BUNDLE_KEY, userNote.text.toString())
    }

    private fun loadServices() {        //načitam ponukane služby z databázy kvôli dialogu na výber služby
        FirebaseDatabase.getInstance().getReference("Services").get().addOnCompleteListener {
            if (it.isSuccessful) {
                services = Array<String>(it.result!!.childrenCount.toInt()) { p0 -> "" }         //všetky slulžby
                checktServices = BooleanArray(it.result!!.childrenCount.toInt()) { p0 -> false}      //inicializujem boolean pole pre ukladanie zvolenych služieb
                var i = 0
                it.result!!.children.forEach {
                    services[i] = it.key.toString()
                    i++
                }
            }
        }
    }

    private fun showServicePickerDialog() { //dialog

        dialogBuilder.setMultiChoiceItems(services, checktServices , DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->   //vytvorim dialog s možnosťami
            if (isChecked)
                selectedServices.add(services[which])
            else if (selectedServices.contains(services[which]))
                selectedServices.remove(services[which])
        })
        dialogBuilder.setPositiveButton("Hotovo", DialogInterface.OnClickListener { dialog, which ->        // button na potvrdenie
            var Services = ""

            for (i in 0..selectedServices.size) {
                if(i < selectedServices.size) {
                    Services += selectedServices[i]
                    if (i < selectedServices.size - 1)
                        Services += ", "
                }
            }
            chooosenService.setText(Services)
            reserevation_price.setText("0 €")
            var price = 0
            for (service in selectedServices) {
                FirebaseDatabase.getInstance().getReference("Services/${service}/price").get().addOnSuccessListener {
                    price += it.value.toString().toInt()
                    reserevation_price.setText(price.toString().plus(" €"))
                }
            }
            reserevation_duration.setText("0 minút")
            var duration = 0
            for (service in selectedServices) {
                FirebaseDatabase.getInstance().getReference("Services/${service}/duration").get().addOnSuccessListener {
                    duration += it.value.toString().toInt()
                    reserevation_duration.setText(duration.toString().plus(" minút"))
                }
            }

        })
        dialogBuilder.setNegativeButton("Zruš", null)       //navrat button

        //dialogBuilder.show()
        val dialog: AlertDialog = dialogBuilder.create()
        dialog.show()
    }


    private fun setData(reservation: Reservation?) {        // ak upravujem nejaku rezerváciu tak nastavím zvolene hodnoty

        choosenDate.text = reservation!!.date
        choosenTime.text = reservation.startTime
        chooosenService.setText(reservation.service)
        userNote.setText(reservation.userNote)
    }

    private fun submitReservation() {    // po kliknuti na tlačidlo začína overenie či môže byť reservacia vytvorena
        val userID = FirebaseAuth.getInstance().uid
        val services = chooosenService.text.toString()
        if(userID.isNullOrBlank()){
            Toast.makeText(this@ReservationActivity, "Pred vytvorením rezervácie sa musíte prihlásiť.", Toast.LENGTH_SHORT).show()
        }else{
            if(day.isEmpty() || startHour.isEmpty() || services.isNullOrEmpty()){
                Toast.makeText(this@ReservationActivity, "Prosím skontrolujte zadané údaje.", Toast.LENGTH_SHORT).show()
            }else {
                val firebaseDate = day.plus(",").plus(month).plus(",").plus(year)
                val ref = FirebaseDatabase.getInstance().getReference("Reservation").child(firebaseDate)        //načítanie všetkych rezervácii toho dátumu
                ref.get().addOnSuccessListener { reservations ->
                    dayReservations = reservations
                    getServicesDuration()


                        }

                }
            }

    }

    private fun getServicesDuration() {     //nacitam služby a spocitam dlžku

        FirebaseDatabase.getInstance().getReference("Services").get()
            .addOnCompleteListener { services ->
                duration = 0
                services.result!!.children.forEach {
                    if (selectedServices.contains(it.key))
                        duration += it.child("duration").value.toString().toInt()
                }
                getOpenHours()
            }
    }

    fun getOpenHours() {    //nacitam otvaraciu dobu aj pre specialne datumy
        val calendar = Calendar.getInstance()
        calendar.set(year.toInt(), month.toInt() - 1, day.toInt())
        val dayInWeek = calendar.get(Calendar.DAY_OF_WEEK).toString()
        FirebaseDatabase.getInstance().getReference("OpenHours/${dayInWeek}").get().addOnSuccessListener{
            weekDay = it.getValue(Day::class.java)!!
            val firebaseDate = day.plus(",").plus(month).plus(",").plus(year)
            FirebaseDatabase.getInstance().getReference("SpecialOpenHours/${firebaseDate}").get()
                .addOnSuccessListener {
                    specialDayStartTime = it.child("startTime")?.value.toString()
                    specialDayEndTime = it.child("endTime")?.value.toString()
                    val availability = checkTimeAvailability(dayReservations,duration,weekDay,specialDayStartTime,specialDayEndTime)
                    val sharedPref = getSharedPreferences("Data", AppCompatActivity.MODE_PRIVATE)
                    val adminID: String? = sharedPref.getString("AdminID", "Error")

                    if (FirebaseAuth.getInstance().uid == adminID || availability) {
                        saveNewReservation()
                    }


                }
                .addOnFailureListener {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    fun saveNewReservation() {      //uložim novu rezervaciu
        val userID = FirebaseAuth.getInstance().uid
        val services = chooosenService.text.toString()
        val note = userNote.text.toString()
        val firebaseDate = day.plus(",").plus(month).plus(",").plus(year)

        val ref = FirebaseDatabase.getInstance().getReference("Reservation").child(firebaseDate)
        val resID = ref.push().key  //vygenure nove ID
        resID?.let { it1 ->
            val reservation = Reservation(it1, userID, services, date, startTime, endTime, note)
            ref.child(it1).setValue(reservation)
                .addOnSuccessListener {
                    if (intent.hasExtra("Reservation")) {
                        Toast.makeText(this@ReservationActivity, "Rezervácia urpavená.", Toast.LENGTH_SHORT).show()
                        removeOldReservation(intent.getParcelableExtra<Reservation>("Reservation"))             //vymazem staru rezervaciu
                    }
                    Toast.makeText(this@ReservationActivity, "Rezervácia vytvorená.", Toast.LENGTH_SHORT).show()

                }

                .addOnFailureListener {
                    Toast.makeText(this@ReservationActivity, it.toString(), Toast.LENGTH_SHORT).show()
                }
            FirebaseFirestore.getInstance().collection("Reservations").document(it1).set(reservation)
        }

    }

    private fun removeOldReservation(oldReservation: Reservation?) {

        val splitDate = oldReservation!!.date!!.split(".")

        val oldResDate = splitDate.component1().plus(",").plus(splitDate.component2()).plus(",").plus(splitDate.component3())//ziskam datum
        FirebaseDatabase.getInstance().getReference("Reservation").child(oldResDate).child(oldReservation.reservationID.toString()).removeValue()
        FirebaseFirestore.getInstance().collection("Reservations").document(oldReservation.reservationID.toString()).delete()
    }

    //kontrola či sa môže rezervácia vytvoriť v datom datume a čase
    private fun checkTimeAvailability(reservations: DataSnapshot, duration: Int, dayHours: Day?, startTime: String, endTime: String): Boolean {
        val newHourStart = startHour.toInt() * 100
        val newMinuteStart = startMinute.toInt()        //zvolený čas pre začiatok nvoej rezervácie

        var finMin: Int = newMinuteStart + duration
        var finHour: Int = startHour.toInt()

        if (finMin >= 60) {     //prepočitanie času
            finHour += finMin / 60
            finMin %= 60
        }
        endMinute = finMin.toString()
        endHour = finHour.toString()
        if(finMin < 10)
            endMinute = "0".plus(finMin.toString())
        if(finHour < 10)
            endHour = "0".plus(finHour.toString())
        val newTimeStart = newHourStart + newMinuteStart
        val newTimeEnd = (finHour * 100) + finMin


        val choosenDate = Calendar.getInstance()
        choosenDate.set(year.toInt(), month.toInt() - 1, day.toInt(), newHourStart, newMinuteStart) // nastavim si kalendar na zvoleny datum a čas aby som mohol skontrlovať ci neni v minulosti
        val currentDate = Calendar.getInstance()

        if (choosenDate.time <= currentDate.time) {
            Toast.makeText(this, "Prepáčte ale zvolili ste čas v minulosti.", Toast.LENGTH_SHORT)
                    .show()
            return false
        }

        currentDate.add(Calendar.HOUR,1)
        if (choosenDate.time < (currentDate.time)) {
            Toast.makeText(this, "Prepáčte ale rezerváciu musíte vytvoriť minimálne hodinu dopredu.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (startTime != "null"|| endTime != "null") {
            if(!startTime.contains(':') || !endTime.contains(':')){
                Toast.makeText(this, "Nie je možné vytvoriť rezerváciu na daný termín, prosím, kontaktujte náš salón.", Toast.LENGTH_SHORT).show()
                return false
            }else {

                val dayStartSplit = startTime.split(':')
                val dayEndSplit = endTime.split(':')
                val dayStart = (dayStartSplit.component1().toInt() * 100) + dayStartSplit.component2().toInt()
                val dayEnd = (dayEndSplit.component1().toInt() * 100) + dayEndSplit.component2().toInt()
                if (newTimeStart !in dayStart..dayEnd || newTimeEnd !in dayStart..dayEnd) {
                    Toast.makeText(this, "Pokúšate sa vytvoriť rezerváciu v čase keď je prevádzka zatvorená.", Toast.LENGTH_SHORT).show()
                    return false
                }
            }

        } else {

                if(dayHours?.startMinute?.isBlank()!! ||dayHours?.endMinute?.isBlank()!!) {
                    Toast.makeText(this, "Nie je možné vytvoriť rezerváciu na daný termín, prosím, kontaktujte náš salón.", Toast.LENGTH_SHORT).show()
                    return false

                }else{
                    val dayStart = (dayHours.startHour?.toInt()!! * 100) + dayHours.startMinute.toInt()
                    val dayEnd = (dayHours.endHour?.toInt()!! *100) + dayHours.endMinute.toInt()
                    if (newTimeStart !in dayStart..dayEnd || newTimeEnd !in dayStart..dayEnd) {
                        Toast.makeText(this, "Pokúšate sa vytvoriť rezerváciu v čase keď je prevádzka zatvorená.", Toast.LENGTH_SHORT).show()
                        return false
                    }
                }

        }

        var oldRes:Reservation? = null

        if (intent.hasExtra("Reservation")) {
            oldRes = intent.getParcelableExtra<Reservation>("Reservation")
        }

        reservations.children?.forEach {
           // prejdem všetky rezervacie z toho dňa či sa neprekryvaju, ak je to moja ktoru upravujem tak ju ignorujem
            if(oldRes != null  && oldRes.reservationID != it.child("reservationID").value.toString()) {
                val startTimeSplit = it.child("startTime").value.toString().split(":")     // vytiahnem si hodiny a minuty a skontrolujem či sa prekryvaju
                val reservedHourStart = startTimeSplit.component1().toInt() * 100
                val reservedMinuteStart = startTimeSplit.component2().toInt()

                val endTimeSplit = it.child("endTime").value.toString().split(":")
                val reservedHourEnding = endTimeSplit.component1().toInt() * 100
                val reservedMinuteEnding = endTimeSplit.component2().toInt()

                val reservedTimeStart = reservedHourStart + reservedMinuteStart
                val reservedTimeEnd = reservedHourEnding + reservedMinuteEnding

                if (newTimeStart in reservedTimeStart..reservedTimeEnd || newTimeEnd in reservedTimeStart..reservedTimeEnd) {
                    Toast.makeText(this, "Bohužiaľ termín je už zabratý.", Toast.LENGTH_SHORT).show()
                    return false
                }
            }
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

    private  fun saveDate(){        //len uložím datum
        date = this.choosenDate.text.toString()
        val splitDate = date.split(".")
        day = splitDate.component1()
        month = splitDate.component2()
        year = splitDate.component3()

    }

    private fun saveTime(){     // uložim čas
        startTime = choosenTime.text.toString()
        val splitTime = startTime.split(":")
        startHour = splitTime.component1()
        startMinute = splitTime.component2()
    }


    fun showTimePickerDialog(v: View) {
        TimePickerFragment().show(supportFragmentManager, "timePicker")

    }

    fun showDatePickerDialog(v: View) {
        DatePickerFragment().show(supportFragmentManager, "datePicker")
    }

}


