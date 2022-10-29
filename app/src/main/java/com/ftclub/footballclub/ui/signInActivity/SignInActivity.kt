package com.ftclub.footballclub.ui.signInActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ftclub.footballclub.R

class SignInActivity: AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_in)
        supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
        window.statusBarColor = resources.getColor(R.color.white, resources.newTheme())
    }

    override fun onBackPressed() {}
}