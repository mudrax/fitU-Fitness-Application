package com.mudrax.fitu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.mudrax.fitu.Constants.USER_NAME
import com.mudrax.fitu.Constants.emailID_of_USER
import com.mudrax.fitu.databinding.ActivityHomePageBinding

class HomePage : AppCompatActivity() {
    private var binding: ActivityHomePageBinding?=null
    lateinit var toggle: ActionBarDrawerToggle // lateinitvar promises that we will put some value in this variable in future but now let it empty


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding?.root)




        toggle = ActionBarDrawerToggle(this , binding?.drawerLayout, R.string.open ,R.string.close)
        binding?.drawerLayout?.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true) // converts drawer to backButton when drawer is opened

        //how to respond to menu item clicks
        binding?.navView?.setNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.menuUserProfile-> {
                    //Toast.makeText(applicationContext, "menu exercise 1", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@HomePage , UserProfileActivity::class.java)
                    startActivity(intent)
                }
                R.id.menuExercise-> {
                    Toast.makeText(applicationContext, "menu exercise 1", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@HomePage , ExerciseActivity::class.java)
                    startActivity(intent)
                }
                R.id.menuNews-> {
                val intent = Intent(this@HomePage , NewsActivity::class.java)
                startActivity(intent)
                }
                R.id.menuShop-> {
                    val intent = Intent(this@HomePage , ShopActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.menuHome-> Toast.makeText(applicationContext, "You are already here", Toast.LENGTH_SHORT).show()
                R.id.menuProfile-> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this , MainActivity::class.java)
                    startActivity(intent)
                    finish()


                }
                R.id.menuFeedback->{
                    val intent = Intent(this@HomePage , FeedbackActivity::class.java)
                    startActivity(intent)
                }
                R.id.menuShare->{
                    Toast.makeText(this, (emailID_of_USER)
                            + (Constants.USER_NAME), Toast.LENGTH_SHORT).show()
                }
            }
            true // since this is a lambda expression return true means we handled clicks
        }

        binding?.flBMI?.setOnClickListener {
            val intent:Intent = Intent(this ,BMI::class.java )
            startActivity(intent)
        }
        binding?.btnHistory?.setOnClickListener {
            val intent:Intent = Intent(this ,HistoryActivity::class.java )
            startActivity(intent)
        }

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