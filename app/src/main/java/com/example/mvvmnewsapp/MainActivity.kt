package com.example.mvvmnewsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.mvvmnewsapp.db.ArticleDatabase
import com.example.mvvmnewsapp.repository.NewsRepository
import com.example.mvvmnewsapp.ui.bottomNav.NewsViewModel
import com.example.mvvmnewsapp.ui.bottomNav.NewsViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        viewModel = ViewModelProvider(
            this,
            NewsViewModelFactory(application, NewsRepository(ArticleDatabase.getInstance(this)))
        )[NewsViewModel::class.java]

        if (!viewModel.hasInternetConnection()){
            Toast.makeText(applicationContext, "No internet connection", Toast.LENGTH_SHORT).show()
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcvNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNav.setupWithNavController(navController)

    }
}