package com.mudrax.fitu

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.mudrax.fitu.databinding.ActivityFeedbackBinding

class FeedbackActivity : AppCompatActivity() {

    private var binding:ActivityFeedbackBinding?=null
    private lateinit var database : FirebaseFirestore
    lateinit var toggle: ActionBarDrawerToggle // lateinitvar promises that we will put some value in this variable in future but now let it empty


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        toggle = ActionBarDrawerToggle(this , binding?.drawerLayout, R.string.open ,R.string.close)
        binding?.drawerLayout?.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding?.navView?.setNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.menuUserProfile-> {
                    //Toast.makeText(applicationContext, "menu exercise 1", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this , UserProfileActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.menuExercise-> {
                    //Toast.makeText(applicationContext, "menu exercise 1", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this , ExerciseActivity::class.java)
                    startActivity(intent)
                }
                R.id.menuNews-> {
                    val intent = Intent(this , NewsActivity::class.java)
                    startActivity(intent)

                }
                R.id.menuShop-> {
                    val intent = Intent(this , ShopActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.menuHome->{
                    val intent = Intent(this , HomePage::class.java)
                    startActivity(intent)
                    finish()

                }
                R.id.menuProfile-> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this , MainActivity::class.java)
                    startActivity(intent)
                    finish()


                }
                R.id.menuFeedback->{
                    Toast.makeText(this, "You're already here", Toast.LENGTH_SHORT).show()
                }
            }
            true // since this is a lambda expression return true means we handled clicks
        }

        binding?.btnFeedback?.setOnClickListener {
            if(binding?.feedback?.text.toString().isNotEmpty())
            {
                val fb = binding?.feedback?.text.toString()
                val feedbackData = FeedbackData(fb , getUserFirebaseId())
                database = FirebaseFirestore.getInstance()

                database.collection("Feedback").document(getUserFirebaseId()).set(feedbackData , SetOptions.merge()).addOnSuccessListener {
                    Toast.makeText(this, "feedback successfully received", Toast.LENGTH_SHORT).show()

                }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to submit", Toast.LENGTH_SHORT).show()
                    }
            }
            else
            {
                Toast.makeText(this, "Empty fields not allowed", Toast.LENGTH_SHORT).show()
            }
        }


    }
    private fun getUserFirebaseId(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }


}