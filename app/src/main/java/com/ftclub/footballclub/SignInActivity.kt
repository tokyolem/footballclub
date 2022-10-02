package com.ftclub.footballclub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController

class SignInActivity: AppCompatActivity() {

    private lateinit var navController: NavController
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_in)
        navController = findNavController(R.id.nav_host_fragment)

        supportActionBar?.hide()
    }
}