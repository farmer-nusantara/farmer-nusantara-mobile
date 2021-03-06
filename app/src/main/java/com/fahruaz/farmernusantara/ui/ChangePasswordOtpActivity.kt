package com.fahruaz.farmernusantara.ui

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.fahruaz.farmernusantara.R
import com.fahruaz.farmernusantara.databinding.ActivityChangePasswordOtpBinding
import com.fahruaz.farmernusantara.ui.customviews.GenericKeyEvent
import com.fahruaz.farmernusantara.ui.customviews.GenericTextWatcher

class ChangePasswordOtpActivity : AppCompatActivity() {

    private var binding: ActivityChangePasswordOtpBinding? = null
    private var customProgressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordOtpBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // toolbar
        setSupportActionBar(binding?.tbChangePasswordOtp)
        if(supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.tbChangePasswordOtp?.setNavigationOnClickListener {
            onBackPressed()
        }

        ChangePasswordEmailActivity.changePasswordViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        ChangePasswordEmailActivity.changePasswordViewModel.toast.observe(this) {
            if(it.isNotEmpty()) {
                showToast(it)
                if(it == "Kode OTP benar") {
                    val intent = Intent(this, ChangePasswordActivity::class.java)
                    startActivity(intent)
                }
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

        binding?.verificationResend?.setOnClickListener {
            ChangePasswordEmailActivity.changePasswordViewModel.sendCode(ChangePasswordEmailActivity.emailForResetPassword)
        }

        binding?.btVerifyCode?.setOnClickListener {
            if(binding?.otpET1?.text.toString().isNotEmpty() && binding?.otpET2?.text.toString().isNotEmpty() &&
                binding?.otpET3?.text.toString().isNotEmpty() && binding?.otpET4?.text.toString().isNotEmpty()
                && binding?.otpET5?.text.toString().isNotEmpty()) {
                val otp = "${binding?.otpET1?.text.toString()}${binding?.otpET2?.text.toString()}${binding?.otpET3?.text.toString()}" +
                        "${binding?.otpET4?.text.toString()}${binding?.otpET5?.text.toString()}"
                ChangePasswordEmailActivity.changePasswordViewModel.checkingTokenResetPassword(otp.toInt())
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

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}