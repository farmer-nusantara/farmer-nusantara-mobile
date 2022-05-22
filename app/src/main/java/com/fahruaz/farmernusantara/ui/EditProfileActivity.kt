package com.fahruaz.farmernusantara.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.fahruaz.farmernusantara.R
import com.fahruaz.farmernusantara.ViewModelFactory
import com.fahruaz.farmernusantara.databinding.ActivityEditProfileBinding
import com.fahruaz.farmernusantara.models.UserModel
import com.fahruaz.farmernusantara.preferences.UserPreferences
import com.fahruaz.farmernusantara.viewmodels.EditProfileViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class EditProfileActivity : AppCompatActivity() {

    private var binding: ActivityEditProfileBinding? = null
    private lateinit var user: UserModel
    private lateinit var editProfileViewModel: EditProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.tbEditProfile)
        if(supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.tbEditProfile?.setNavigationOnClickListener {
            onBackPressed()
        }

        setupViewModel()

        editProfileViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        editProfileViewModel.toast.observe(this) {
            showToast(it)
        }

        binding?.nameEditText?.setText(MainActivity.userModel?.name!!)
        binding?.emailEditText?.setText(MainActivity.userModel?.email!!)
        binding?.phoneEditText?.setText(MainActivity.userModel?.phone!!)

        binding?.btSaveProfile?.setOnClickListener{
            editUser()
        }
    }

    private fun setupViewModel() {
        editProfileViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore), this)
        )[EditProfileViewModel::class.java]

    }

    private fun editUser() {

        val name = binding?.nameEditText?.text.toString()
        val email = binding?.emailEditText?.text.toString()
        val phone = binding?.phoneEditText?.text.toString()

        showLoading(true)

        user = UserModel(email = email, name = name, phone = phone)
        editProfileViewModel.editUserData(user)

        editProfileViewModel.toast.observe(this){
            if (it == "Berhasil edit profil"){
                AlertDialog.Builder(this@EditProfileActivity).apply {
                    setTitle(resources.getString(R.string.success))
                    setMessage(resources.getString(R.string.user_data_was_edited))
                    setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                        val intent = Intent(this@EditProfileActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                    create()
                    show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.progressBar?.visibility = View.VISIBLE
        } else {
            binding?.progressBar?.visibility = View.GONE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}