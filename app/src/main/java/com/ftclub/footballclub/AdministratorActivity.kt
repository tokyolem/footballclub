package com.ftclub.footballclub

import android.os.Bundle
import android.view.Menu
import android.widget.FrameLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ftclub.footballclub.databinding.ActivityAdministratorBinding
import com.google.android.material.navigation.NavigationView


class AdministratorActivity: AppCompatActivity() {

    private lateinit var binding: ActivityAdministratorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdministratorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_admin_main)
        val navView = binding.navViewAdmin
        val drawerLayout = binding.drawerLayoutAdmin

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.accountsFragment, R.id.userSettingsFragment
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        burgerButtonAction()
        signOutFromAccount()
    }

    private fun burgerButtonAction() {
        val burgerButton = findViewById<FrameLayout>(R.id.burger_button)

        burgerButton.setOnClickListener {
            binding.drawerLayoutAdmin.openDrawer(binding.navViewAdmin, true)
        }
    }

    private fun signOutFromAccount() {
        findViewById<FrameLayout>(R.id.log_out).setOnClickListener {
            finish()
        }
    }
}