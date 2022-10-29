package com.ftclub.footballclub.ui.administratorActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ftclub.footballclub.R
import com.ftclub.footballclub.databinding.ActivityAdministratorBinding

class AdministratorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdministratorBinding

    private val navOptions by lazy {
        NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_right)
            .setPopEnterAnim(R.anim.slide_in_left)
            .setPopExitAnim(R.anim.slide_out_left)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdministratorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        supportActionBar!!.hide()

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
        binding.navView.setupWithNavController(navController)

        binding.navView.setOnItemSelectedListener { item ->
            navController.navigate(item.itemId, null, navOptions.build())
            true
        }
    }

/*<include
android:id="@+id/app_bar_main"
layout="@layout/app_bar_content"
android:layout_width="match_parent"
android:layout_height="match_parent" />*/
}