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
import com.bachelor.reservation.adapters.reviewAdapter
import com.bachelor.reservation.classes.Review
import com.bachelor.reservationapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.fragment_reviews.*
import kotlinx.android.synthetic.main.fragment_reviews.view.*
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
            val userID = FirebaseAuth.getInstance().currentUser.uid.toString()
            val reviewText = newReviewForm.text.toString()

            val ref = FirebaseDatabase.getInstance().getReference("Reviews")
            val revID = ref.push().key
            val review = Review(userID, userName.toString(),reviewText)
            revID?.let {
                ref.child(it).setValue(review).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(context, "Recenzia pridaná", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context, it.exception.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }


    private  fun listReviews() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val ReviewsList = mutableListOf<Review>()

            val ref = FirebaseDatabase.getInstance().getReference("Reviews")
            ref.get().addOnCompleteListener {
                it.result!!.children?.forEach {
                    val review = Review(
                            it.child("authorID").value.toString(),
                            it.child("authorName").value.toString(),
                            it.child("text").value.toString(),
                    )
                    ReviewsList.add(review)
                }
                val revAdapter = reviewAdapter(ReviewsList)
                recyclerViewReviwesItems.apply {
                    adapter = revAdapter
                    layoutManager = LinearLayoutManager(context)
                }
            }

        } catch (e: Exception){
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }



}