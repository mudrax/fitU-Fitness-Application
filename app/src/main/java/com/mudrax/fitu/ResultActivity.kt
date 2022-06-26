package com.mudrax.fitu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.mudrax.fitu.databinding.ActivityResultBinding
import forNews.NewsApplication
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ResultActivity : AppCompatActivity() {

    private var binding: ActivityResultBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //step 1
        setSupportActionBar(binding?.toolbarResult)//layout starts supporting toolbar
        //making a back button

        if(supportActionBar!=null)
        {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }//step 2 of back button

        binding?.toolbarResult?.setNavigationOnClickListener{
            onBackPressed()
        }//step 1 : what function back button will do

        binding?.btnFinish?.setOnClickListener{
            finish()
        }

        val dao = (application as NewsApplication).db.historyDao()
        addDateToDatabase(dao)

    }


    fun addDateToDatabase(historyDao: HistoryDao) {

        val c = Calendar.getInstance()
        val dateTime = c.time
        Log.e("date: ",""+dateTime)

        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss" , Locale.getDefault())
        val date = sdf.format(dateTime)
        Log.e("Formatted date: ",""+date)

        lifecycleScope.launch {
            historyDao.insert(HistoryEntity(date))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}