package com.mudrax.fitu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mudrax.fitu.databinding.ActivityNewsBinding

import forNews.NewsViewModel
import forNews.NewsViewModelProviderFactory
import forNews.db.ArticleDatabase
import forNews.repository.NewsRepository


class NewsActivity : AppCompatActivity() {


        lateinit var viewModel: NewsViewModel
        private var binding: ActivityNewsBinding?=null
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityNewsBinding.inflate(layoutInflater)
            setContentView(binding?.root)

            val newsRepository = NewsRepository(ArticleDatabase(this))
            val viewModelProviderFactory = NewsViewModelProviderFactory(application ,  newsRepository )
            viewModel = ViewModelProvider(this , viewModelProviderFactory).get(NewsViewModel::class.java)

            val navHostFragment = supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
            //val navController = navHostFragment.navController

            binding?.bottomNavigationView?.setupWithNavController(navHostFragment.navController)
            //binding?.bottomNavigationView?.setupWithNavController(findNavController(R.id.newsNavHostFragment))


        }




}