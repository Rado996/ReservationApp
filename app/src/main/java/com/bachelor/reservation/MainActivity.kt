package com.bachelor.reservationapp

import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.widget.CalendarView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bachelor.reservation.adapters.pictureAdapter
import com.bachelor.reservation.adapters.reservationAdapter
import com.bachelor.reservation.classes.Reservation
import com.bachelor.reservation.fragments.LocationFragment
import com.bachelor.reservationapp.adapters.ViewPagerAdapter
import com.bachelor.reservationapp.classes.Picture
import com.bachelor.reservationapp.fragments.CalendarFragment
import com.bachelor.reservationapp.fragments.GalleryFragment
import com.bachelor.reservationapp.fragments.HomeFragment
import com.bachelor.reservationapp.fragments.ReviewsFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        database = Firebase.database.reference

        if(FirebaseAuth.getInstance().currentUser == null){
            FirebaseAuth.getInstance().signInWithEmailAndPassword("aaaaaa@gmail.com","123456")
        }

        val bar: androidx.appcompat.widget.Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(bar)
        bar.showOverflowMenu()
        setupTabs()
        listPictures()



//        val calenda: CalendarView = findViewById(R.id.calendarViewReservations)
//
//        calenda.setOnDateChangeListener { caleda, year, month, dayOfMonth ->
//            val selectedDate = dayOfMonth.toString().plus(",").plus(month.toString()).plus(",").plus(year.toString())
//            listReservations(selectedDate)
//        }

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

    }

    private  fun listPictures() = CoroutineScope(Dispatchers.IO).launch {
        try {
            //val images = Firebase.storage.reference
            val picuresList = mutableListOf<Picture>()

            FirebaseDatabase.getInstance().getReference("Pics").get().addOnCompleteListener {
                Log.e("PICS", it.toString())
                it.result?.children?.forEach {
                    Log.e("Picture",it.child("link").value.toString())
                    Log.e("Picture",it.child("description").value.toString())
                    picuresList.add(Picture(it.key,it.child("link").value.toString(),it.child("description").value.toString()))
                }
                val pictureAdapter = pictureAdapter(picuresList)
                recycler_view_gallery_items.apply {
                    adapter = pictureAdapter
                    layoutManager = LinearLayoutManager(this@MainActivity)
                }
            }
//            withContext(Dispatchers.Main){
//
//            }

        } catch (e : Exception){
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private  fun listReservations(selectedDate: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val reservationList = mutableListOf<Reservation>()

            FirebaseDatabase.getInstance().getReference("Rezervations").child(selectedDate).get().addOnCompleteListener {

                it.result?.children?.forEach {

                    val reservation = it.getValue(Reservation::class.java)
                    reservation?.let { it1 -> reservationList.add(it1) }
                    //reservationList.add(Picture(it.key,it.child("link").value.toString(),it.child("description").value.toString()))
                }
                val reservationAdapter = reservationAdapter(reservationList)
                recyclerViewReservationItems.apply {
                    adapter = reservationAdapter
                    layoutManager = LinearLayoutManager(this@MainActivity)
                }
            }

        } catch (e : Exception){
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)
        return true
    }

    private fun setupTabs(){
        val adapter = ViewPagerAdapter(supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.viewPager)
        val tabs: TabLayout = findViewById(R.id.tabs)
        adapter.addFragment(HomeFragment(),title = "")
        adapter.addFragment(GalleryFragment(),title = "")
        adapter.addFragment(CalendarFragment(), title = "")
        adapter.addFragment(LocationFragment(), title = "")
        adapter.addFragment(ReviewsFragment(), title = "")


        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_baseline_home_24)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_baseline_photo_24)
        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_baseline_calendar_today_24)
        tabs.getTabAt(3)!!.setIcon(R.drawable.ic_baseline_location_on_24)
        tabs.getTabAt(4)!!.setIcon(R.drawable.ic_baseline_star_rate_24)


    }

}


