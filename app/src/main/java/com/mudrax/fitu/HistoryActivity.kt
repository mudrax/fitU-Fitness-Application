package com.mudrax.fitu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mudrax.fitu.databinding.ActivityHistoryBinding
import forNews.NewsApplication
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private var binding: ActivityHistoryBinding?= null// step 1 ... step 0 is to make viewBinding true in gradle project
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)// step 2
        setContentView(binding?.root)


        //step 1
        setSupportActionBar(binding?.toolbarHistory)//layout starts supporting toolbar
        //making a back button

        if(supportActionBar!=null)
        {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "YOUR HISTORY"
        }//step 2 of back button

        binding?.toolbarHistory?.setNavigationOnClickListener{
            onBackPressed()
        }//step 1 : what function back button will do

        val dao = (application as NewsApplication).db.historyDao()
        getAllCompletedDates(dao)
    }

    private fun getAllCompletedDates(historyDao: HistoryDao)
    {
        lifecycleScope.launch {
            historyDao.fetchAllDates().collect { allCompletedDates->
                if(allCompletedDates.isNotEmpty())
                {
                    binding?.noDataAvailable?.visibility = View.GONE
                    binding?.rvHistory?.visibility = View.VISIBLE
                    binding?.rvHistory?.layoutManager = LinearLayoutManager(this@HistoryActivity)

                    var list = ArrayList<String>()
                    for(i in allCompletedDates)
                    {
                        list.add(i.date)
                    }
                    val historyAdapter = HistoryAdapter(list)
                    binding?.rvHistory?.adapter = historyAdapter
                }
                else
                {
                    binding?.rvHistory?.visibility = View.GONE
                    binding?.noDataAvailable?.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}