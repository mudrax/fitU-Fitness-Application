package com.mudrax.fitu

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.firebase.auth.FirebaseAuth
import com.mudrax.fitu.databinding.ActivityShopBinding

class ShopActivity : AppCompatActivity() {
    private var binding:ActivityShopBinding?=null
    lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopBinding.inflate(layoutInflater)
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
                }
                R.id.menuExercise-> {
                    Toast.makeText(applicationContext, "menu exercise 1", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@ShopActivity , ExerciseActivity::class.java)
                    startActivity(intent)
                }
                R.id.menuNews-> {
                    val intent = Intent(this@ShopActivity , NewsActivity::class.java)
                    startActivity(intent)
                }
                R.id.menuShop-> {Toast.makeText(this@ShopActivity, "You are already here" , Toast.LENGTH_SHORT).show()}
                R.id.menuHome-> {
                    val intent = Intent(this@ShopActivity , HomePage::class.java)
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
                    val intent = Intent(this , FeedbackActivity::class.java)
                    startActivity(intent)
                }
                //R.id.menuProfile-> Toast.makeText(applicationContext, "menu profile 3", Toast.LENGTH_SHORT).show()
            }
            true // since this is a lambda expression return true means we handled clicks
        }

        binding?.ivFlipkart?.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW , Uri.parse("https://www.flipkart.com/offers-list/sportsfitnessequipment?screen=dynamic&pk=themeViews%3DSF-SportFitness-OMU%3AApp%2CSF-SportFitness-OMU%3ADesk~widgetType%3DdealCard~contentType%3Dneo&wid=2.dealCard.OMU&fm=neo%2Fmerchandising&iid=M_05e6dd9d-cc8b-4e05-9b41-1bee95c10468_1_372UD5BXDFYS_MC.S6K5RFX2RYR8&otracker=hp_rich_navigation_7_1.navigationCard.RICH_NAVIGATION_Beauty%252C%2BToys%2B%2526%2BMore~Sports%2B%2526%2BFitness_S6K5RFX2RYR8&otracker1=hp_rich_navigation_PINNED_neo%2Fmerchandising_NA_NAV_EXPANDABLE_navigationCard_cc_7_L1_view-all&cid=S6K5RFX2RYR8"))
            startActivity(intent)
            finish()
        }
        binding?.ivAmazon?.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW , Uri.parse(
                "https://www.amazon.in/Sports/b?ie=UTF8&node=1984443031"))
            startActivity(intent)
            finish()
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