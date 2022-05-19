package com.fahruaz.farmernusantara.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.fahruaz.farmernusantara.R
import com.fahruaz.farmernusantara.ViewModelFactory
import com.fahruaz.farmernusantara.databinding.ActivityChangePasswordEmailBinding
import com.fahruaz.farmernusantara.preferences.UserPreferences
import com.fahruaz.farmernusantara.viewmodels.ChangePasswordViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ChangePasswordEmailActivity : AppCompatActivity() {

    private var binding: ActivityChangePasswordEmailBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordEmailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // toolbar
        setSupportActionBar(binding?.tbChangePasswordEmail)
        if(supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.tbChangePasswordEmail?.setNavigationOnClickListener {
            onBackPressed()
        }

        changePasswordViewModel = obtainChangePasswordViewModel(this)

        changePasswordViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        changePasswordViewModel.toast.observe(this) {
            showToast(it)
            if(it == "Kode OTP berhasil dikirim") {
                val intent = Intent(this, ChangePasswordOtpActivity::class.java)
                startActivity(intent)
            }
        }

        binding?.btSendOtp?.setOnClickListener {
            binding?.emailEditTextLayout?.error = ""
            val email = binding?.emailEditText?.text.toString()
            when {
                email.isEmpty() -> binding?.emailEditTextLayout?.error = resources.getString(R.string.empty_email)
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> binding?.emailEditTextLayout?.error = resources.getString(R.string.invalid_email)
                else -> {
                    emailForResetPassword = email
                    changePasswordViewModel.sendCode(email)
                }
            }
        }
    }

    private fun obtainChangePasswordViewModel(activity: AppCompatActivity): ChangePasswordViewModel {
        val pref = UserPreferences.getInstance(dataStore)
        return ViewModelProvider(activity, ViewModelFactory(pref, this))[ChangePasswordViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.pbChangePasswordEmail?.visibility = View.VISIBLE
        } else {
            binding?.pbChangePasswordEmail?.visibility = View.GONE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        lateinit var changePasswordViewModel: ChangePasswordViewModel
        var emailForResetPassword = ""
    }

}