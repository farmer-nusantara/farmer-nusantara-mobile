package com.fahruaz.farmernusantara.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.fahruaz.farmernusantara.ui.fragment.profile.ProfileFragment
import com.fahruaz.farmernusantara.viewmodels.EditProfileViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class EditProfileActivity : AppCompatActivity() {

    private var binding: ActivityEditProfileBinding? = null
    private var customProgressDialog: Dialog? = null
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

        binding?.nameEditText?.setText(MainActivity.userModel?.name!!)
        binding?.emailEditText?.setText(MainActivity.userModel?.email!!)
        binding?.phoneEditText?.setText(MainActivity.userModel?.phone!!)

        binding?.btSaveProfile?.setOnClickListener{
            editUser()
        }

        binding?.gantiKataSandi?.setOnClickListener {
            val intent = Intent(this, ChangePasswordEmailActivity::class.java)
            startActivity(intent)
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
            ProfileFragment.isEditBtnClicked = true
            if (it == "Berhasil edit profil"){
                AlertDialog.Builder(this@EditProfileActivity).apply {
                    setTitle(resources.getString(R.string.success))
                    setMessage(resources.getString(R.string.user_data_was_edited))
                    setPositiveButton(resources.getString(R.string.ok)) { dialog, _ ->
                        dialog.dismiss()
                        finish()
                    }
                    setCancelable(false)
                    create()
                    show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading)
            showProgressDialog()
        else
            cancelProgressDialog()
    }

    private fun showProgressDialog() {
        customProgressDialog = Dialog(this)
        customProgressDialog?.setContentView(R.layout.dialog_custom_progressbar)
        customProgressDialog?.setCancelable(false)
        customProgressDialog?.show()
    }

    private fun cancelProgressDialog() {
        if (customProgressDialog != null) {
            customProgressDialog?.dismiss()
            customProgressDialog = null
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}