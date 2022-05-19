package com.fahruaz.farmernusantara.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.fahruaz.farmernusantara.R
import com.fahruaz.farmernusantara.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : AppCompatActivity() {

    private var binding: ActivityChangePasswordBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // toolbar
        setSupportActionBar(binding?.tbChangePassword)
        if(supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.tbChangePassword?.setNavigationOnClickListener {
            onBackPressed()
        }

        ChangePasswordEmailActivity.changePasswordViewModel.toast.observe(this) {
            showToast(it)
            if(it == "Berhasil mengganti kata sandi") {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
        }

        binding?.btChangePassword?.setOnClickListener {
            binding?.passwordEditTextLayout?.error = ""
            binding?.passwordConfirmEditTextLayout?.error = ""

            val password = binding?.passwordEditText?.text.toString()
            val passwordConfirmation = binding?.passwordConfirmEditText?.text.toString()

            when {
                password.isEmpty() -> binding?.passwordEditTextLayout?.error = resources.getString(R.string.empty_password)
                passwordConfirmation.isEmpty() -> binding?.passwordEditTextLayout?.error = resources.getString(R.string.empty_repeat_password)
                password.length < 8 -> {
                    binding?.passwordEditTextLayout?.error = resources.getString(R.string.password_lacking)
                }
                passwordConfirmation != password -> {
                    binding?.passwordConfirmEditTextLayout?.error = resources.getString(R.string.different_repeat_password)
                    binding?.passwordConfirmEditText?.text?.clear()
                }
                else -> {
                    ChangePasswordEmailActivity.changePasswordViewModel
                        .changePasswordAccount(ChangePasswordEmailActivity.emailForResetPassword, password, passwordConfirmation)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.pbChangePassword?.visibility = View.VISIBLE
        } else {
            binding?.pbChangePassword?.visibility = View.GONE
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