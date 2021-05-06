package com.bachelor.reservationapp

import android.content.Intent
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.bachelor.reservation.activities.ConversationsActivity
import com.bachelor.reservation.activities.LoginActivity
import com.bachelor.reservation.activities.UserProfileActivity
import com.bachelor.reservation.fragments.LocationFragment
import com.bachelor.reservationapp.adapters.ViewPagerAdapter
import com.bachelor.reservationapp.fragments.CalendarFragment
import com.bachelor.reservationapp.fragments.GalleryFragment
import com.bachelor.reservationapp.fragments.HomeFragment
import com.bachelor.reservationapp.fragments.ReviewsFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.fragment_gallery.*
import java.util.*

// Hlavná aktivita ktorá sa spustí pri otvorení aplikácie

class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        database = Firebase.database.reference


        val bar: androidx.appcompat.widget.Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(bar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        bar.showOverflowMenu()
        setupTabs()

        setupData()

        showMessages.setOnClickListener {
            val intent= Intent(this@MainActivity, ConversationsActivity::class.java)
            startActivity(intent)
        }



        my_toolbar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_Login) {
                if (!FirebaseAuth.getInstance().uid.isNullOrBlank())
                    Toast.makeText(this, "Najskôr sa odhláste!.", Toast.LENGTH_SHORT).show()
                else {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
            if (item.itemId == R.id.action_User) {
                if (FirebaseAuth.getInstance().uid.isNullOrBlank())
                    Toast.makeText(this, "Nie ste prihlásený.", Toast.LENGTH_SHORT).show()
                else {
                    val intent = Intent(this@MainActivity, UserProfileActivity::class.java)
                    startActivity(intent)
                }
            }
            if (item.itemId == R.id.action_Logout) {
                if (FirebaseAuth.getInstance().uid.isNullOrBlank())
                    Toast.makeText(this, "Nie ste prihlásený.", Toast.LENGTH_SHORT).show()
                else
                    FirebaseAuth.getInstance().signOut()
            }
            return@OnMenuItemClickListener false
        })

    }

    private fun setupData() {
        FirebaseDatabase.getInstance().getReference("Admin").get().addOnCompleteListener {
            if (it.isSuccessful){
                val sharedPref = getSharedPreferences("Data", MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putString("AdminID", it.result!!.value.toString())
                editor.apply()
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
        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_baseline_date_range_24)
        tabs.getTabAt(3)!!.setIcon(R.drawable.ic_baseline_location_on_24)
        tabs.getTabAt(4)!!.setIcon(R.drawable.ic_baseline_star_rate_24)


    }

}


