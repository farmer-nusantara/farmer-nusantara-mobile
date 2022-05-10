package com.fahruaz.farmernusantara

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.fahruaz.farmernusantara.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.hide()

        navController = findNavController(R.id.navHostFragment)
        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.farmlandFragment,
            R.id.mapFragment,
            R.id.profileFragment
        ).build()

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding?.bottomNavigationView?.setupWithNavController(navController)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}