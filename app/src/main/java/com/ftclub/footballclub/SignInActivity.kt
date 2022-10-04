package com.ftclub.footballclub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel

class SignInActivity: AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_in)
        supportActionBar?.hide()

        accountsViewModel = ViewModelProviders.of(this)[AccountsViewModel::class.java]
    }

    override fun onBackPressed() {}

    companion object {
        lateinit var accountsViewModel: AccountsViewModel
    }
}