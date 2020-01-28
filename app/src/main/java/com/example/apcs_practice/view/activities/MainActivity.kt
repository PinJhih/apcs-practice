package com.example.apcs_practice.view.activities

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.apcs_practice.R
import com.example.apcs_practice.services.NotificationService

lateinit var settings: SharedPreferences

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_session_select,
                R.id.navigation_histories,
                R.id.navigation_setting
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        settings = getSharedPreferences("settings", Activity.MODE_PRIVATE)

        if(settings.getBoolean("firstInstall",true)){
            val edit = settings.edit()
            edit.putBoolean("firstInstall",false)
            edit.apply()

            val i = Intent(this,NotificationService::class.java)
            startService(i)
        }
    }

    fun checkAnswer(session: Int, answers: String) {
        val i = Intent(this, CheckAnswerActivity::class.java)
        val b = Bundle()
        b.putInt("session", session)
        b.putString("answer", answers)
        i.putExtras(b)
        startActivity(i)
    }
}
