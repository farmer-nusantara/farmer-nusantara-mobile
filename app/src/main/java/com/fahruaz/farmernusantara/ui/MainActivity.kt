package com.fahruaz.farmernusantara.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.fahruaz.farmernusantara.R
import com.fahruaz.farmernusantara.ViewModelFactory
import com.fahruaz.farmernusantara.databinding.ActivityMainBinding
import com.fahruaz.farmernusantara.models.UserModel
import com.fahruaz.farmernusantara.preferences.UserPreferences
import com.fahruaz.farmernusantara.viewmodels.FarmlandViewModel
import com.fahruaz.farmernusantara.viewmodels.ImageStorageViewModel
import com.fahruaz.farmernusantara.viewmodels.MainViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.hide()
        mainViewModel = obtainViewModel(this)
        imageStorageViewModel = ViewModelProvider(this)[ImageStorageViewModel::class.java]

        mainViewModel.getUser().observe(this) { user ->
            if (user?.token?.isEmpty()!!) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }

            userModel = user
            LoginActivity.token = user.token!!
        }

        binding?.bottomNavigationView?.background = null
        binding?.bottomNavigationView?.menu?.getItem(1)?.isEnabled = false

        navController = findNavController(R.id.navHostFragment)
        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.farmlandFragment,
            R.id.mapFragment,
            R.id.profileFragment
        ).build()

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding?.bottomNavigationView?.setupWithNavController(navController)
    }

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val pref = UserPreferences.getInstance(dataStore)
        return ViewModelProvider(activity, ViewModelFactory(pref, this))[MainViewModel::class.java]
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        var userModel: UserModel? = null
        lateinit var mainViewModel: MainViewModel
        lateinit var imageStorageViewModel: ImageStorageViewModel
    }

}