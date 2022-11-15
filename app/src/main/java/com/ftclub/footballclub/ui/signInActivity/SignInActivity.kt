package com.ftclub.footballclub.ui.signInActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ftclub.footballclub.R
import com.ftclub.footballclub.databinding.ActivitySignInBinding

class SignInActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)

        setContentView(binding.root)
        supportActionBar?.hide()
    }
}