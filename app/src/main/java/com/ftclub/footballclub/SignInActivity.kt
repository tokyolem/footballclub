package com.ftclub.footballclub

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.ftclub.footballclub.databinding.ActivitySignInBinding

class SignInActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_in)

        findNavController(R.id.nav_host_fragment)

        supportActionBar?.hide()
    }

    override fun onBackPressed() {}
}