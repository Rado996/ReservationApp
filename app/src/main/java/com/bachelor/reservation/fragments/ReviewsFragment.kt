package com.bachelor.reservationapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bachelor.reservation.classes.Review
import com.bachelor.reservation.viewHolders.reviewViewHolder
import com.bachelor.reservationapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_reviews.*
import kotlinx.android.synthetic.main.fragment_reviews.view.*
import kotlinx.android.synthetic.main.review_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ReviewsFragment : Fragment() {
    private lateinit var viewOfLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        viewOfLayout = inflater.inflate(R.layout.fragment_reviews, container, false)

        listReviews()

        viewOfLayout.reviewSubmitBtn.setOnClickListener {
            submitReview()
        }

        return viewOfLayout
    }

    private fun submitReview() {
        if(FirebaseAuth.getInstance().currentUser == null)
        {
            Toast.makeText(context, "Na pridanie recenzie musíte byť prihlásený.", Toast.LENGTH_SHORT).show()
        }else{
            val sharedPref: SharedPreferences = requireContext().getSharedPreferences("Data", Context.MODE_PRIVATE)
            val userName = sharedPref.getString("userName", "Not logged in")
            val userID = FirebaseAuth.getInstance().currentUser.uid
            val reviewText = newReviewForm.text.toString()

            val ref = FirebaseDatabase.getInstance().getReference("Reviews")
            val review = Review(userID, userName.toString(),reviewText)

            ref.push().setValue(review).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "Recenzia pridaná", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, it.exception.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private  fun listReviews() = CoroutineScope(Dispatchers.IO).launch {
        try {

            val adapter = GroupAdapter<GroupieViewHolder>()
            val ref = FirebaseDatabase.getInstance().getReference("Reviews")
            ref.addChildEventListener(object: ChildEventListener{
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val review = snapshot.getValue(Review::class.java)
                    review?.let {
                        adapter.add(reviewViewHolder(it))
                    }
                    recyclerViewReviwesItems.adapter = adapter
                    recyclerViewReviwesItems.layoutManager = LinearLayoutManager(context)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }


            })


        } catch (e: Exception){
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}

