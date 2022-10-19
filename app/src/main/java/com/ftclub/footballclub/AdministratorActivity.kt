package com.ftclub.footballclub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ftclub.footballclub.databinding.ActivityAdministratorBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdministratorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdministratorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdministratorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        supportActionBar!!.hide()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_accounts,
                R.id.navigation_players,
                R.id.navigation_add,
                R.id.navigation_about_app
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

/*private fun burgerButtonAction() {
    val burgerButton = findViewById<FrameLayout>(R.id.burger_button)

    burgerButton.setOnClickListener {
        binding.drawerLayoutAdmin.openDrawer(binding.navViewAdmin, true)
    }
}*/

/*<include
android:id="@+id/app_bar_main"
layout="@layout/app_bar_content"
android:layout_width="match_parent"
android:layout_height="match_parent" />*/
}