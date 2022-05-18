package com.fahruaz.farmernusantara.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.fahruaz.farmernusantara.ViewModelFactory
import com.fahruaz.farmernusantara.databinding.ActivityVerificationBinding
import com.fahruaz.farmernusantara.preferences.UserPreferences
import com.fahruaz.farmernusantara.ui.customviews.GenericKeyEvent
import com.fahruaz.farmernusantara.ui.customviews.GenericTextWatcher
import com.fahruaz.farmernusantara.viewmodels.MainViewModel
import com.fahruaz.farmernusantara.viewmodels.VerificationViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class VerificationActivity : AppCompatActivity() {

    private var binding: ActivityVerificationBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val verificationViewModel = obtainViewModel(this)
        verificationViewModel.getUser().observe(this) {
            verificationViewModel.sendToken(it.email!!)
        }

        verificationViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        verificationViewModel.toast.observe(this) {
            showToast(it)
            if(it == "Successfully") {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        //GenericTextWatcher here works only for moving to next EditText when a number is entered
        binding?.otpET1?.addTextChangedListener(GenericTextWatcher(binding?.otpET1!!, binding?.otpET2!!))
        binding?.otpET2?.addTextChangedListener(GenericTextWatcher(binding?.otpET2!!, binding?.otpET3!!))
        binding?.otpET3?.addTextChangedListener(GenericTextWatcher(binding?.otpET3!!, binding?.otpET4!!))
        binding?.otpET4?.addTextChangedListener(GenericTextWatcher(binding?.otpET4!!, binding?.otpET5!!))
        binding?.otpET5?.addTextChangedListener(GenericTextWatcher(binding?.otpET5!!, null))

        //GenericKeyEvent here works for deleting the element and to switch back to previous EditText
        binding?.otpET1?.setOnKeyListener(GenericKeyEvent(binding?.otpET1!!, null))
        binding?.otpET2?.setOnKeyListener(GenericKeyEvent(binding?.otpET2!!, binding?.otpET1!!))
        binding?.otpET3?.setOnKeyListener(GenericKeyEvent(binding?.otpET3!!, binding?.otpET2!!))
        binding?.otpET4?.setOnKeyListener(GenericKeyEvent(binding?.otpET4!!, binding?.otpET3!!))
        binding?.otpET5?.setOnKeyListener(GenericKeyEvent(binding?.otpET5!!, binding?.otpET4!!))

        binding?.btValidation?.setOnClickListener {
            val otp = "${binding?.otpET1?.text.toString()}${binding?.otpET2?.text.toString()}${binding?.otpET3?.text.toString()}" +
                    "${binding?.otpET4?.text.toString()}${binding?.otpET5?.text.toString()}"
            verificationViewModel.changeStatusAccount(otp)
        }

        binding?.btSkipValidation?.setOnClickListener {

        }

        binding?.verificationResend?.setOnClickListener {
            verificationViewModel.getUser().observe(this) {
                verificationViewModel.sendToken(it.email!!)
            }
        }

    }

    private fun obtainViewModel(activity: AppCompatActivity): VerificationViewModel {
        val pref = UserPreferences.getInstance(dataStore)
        return ViewModelProvider(activity, ViewModelFactory(pref, this))[VerificationViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.pbVerification?.visibility = View.VISIBLE
        } else {
            binding?.pbVerification?.visibility = View.GONE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}