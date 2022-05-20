package com.fahruaz.farmernusantara.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.fahruaz.farmernusantara.ViewModelFactory
import com.fahruaz.farmernusantara.api.ApiConfig
import com.fahruaz.farmernusantara.databinding.ActivityEditProfileBinding
import com.fahruaz.farmernusantara.preferences.UserPreferences
import com.fahruaz.farmernusantara.response.profile.GetProfileResponse
import com.fahruaz.farmernusantara.viewmodels.EditProfileViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class EditProfileActivity : AppCompatActivity() {

    private var binding: ActivityEditProfileBinding? = null
    private lateinit var user: GetProfileResponse
    private lateinit var editProfileViewModel: EditProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // toolbar
        setSupportActionBar(binding?.tbEditProfile)
        if(supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.tbEditProfile?.setNavigationOnClickListener {
            onBackPressed()
        }

        setupViewModel()

        binding?.btSaveProfile?.setOnClickListener{
            getUser()
        }
    }

    private fun setupViewModel() {
        editProfileViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore), this)
        )[EditProfileViewModel::class.java]

    }

    private fun getUser() {

        val name = binding?.nameEditText?.text.toString()
        val email = binding?.emailEditText?.text.toString()
        val phone = binding?.phoneEditText?.text.toString()


        showLoading(true)
        val client = ApiConfig().getApiService().editProfile("Token ${MainActivity.userModel?.token}",
            MainActivity.userModel?.id!!, email, name, phone)

        client.enqueue(object : Callback<GetProfileResponse> {
            override fun onResponse(
                call: Call<GetProfileResponse>,
                response: Response<GetProfileResponse>
            ) {
                if (response.isSuccessful) {
                    showLoading(false)
                    val responseBody = response.body()
                    if (responseBody != null) {
                        editProfileViewModel.getUser()

                    }
                } else {
                    showLoading(false)
                    Log.e("EditProfile", "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GetProfileResponse>, t: Throwable) {
                showLoading(false)
                Log.e("EditProfile", "Terjadi Kesalahan: ${t.message}")
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.progressBar?.visibility = View.VISIBLE
        } else {
            binding?.progressBar?.visibility = View.GONE
        }
    }

}