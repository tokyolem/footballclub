package com.ftclub.footballclub

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.ftclub.footballclub.basic.room.BaseDatabase
import com.ftclub.footballclub.databinding.ActivitySignInBinding

class SignInActivity: AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        BaseDatabase.init(applicationContext)

        setContentView(R.layout.activity_sign_in)
        supportActionBar?.hide()
    }

    override fun onBackPressed() {}
}