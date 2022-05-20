package com.uacam.pitbullmanagerapp.student

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.uacam.pitbullmanagerapp.R

class StudentSessionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_session)
        val navView: BottomNavigationView = findViewById(R.id.navBottomNavigationView)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        AppBarConfiguration(navController.graph)
        navView.setupWithNavController(navController)
    }
}