package com.fahruaz.farmernusantara.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.fahruaz.farmernusantara.R
import com.fahruaz.farmernusantara.ViewModelFactory
import com.fahruaz.farmernusantara.databinding.ActivityRegisterBinding
import com.fahruaz.farmernusantara.models.UserModel
import com.fahruaz.farmernusantara.preferences.UserPreferences
import com.fahruaz.farmernusantara.viewmodels.RegisterViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()

        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        registerViewModel.toast.observe(this) {
            showToast(it)
        }

        action()
        setupView()
    }

    private fun setupViewModel() {
        registerViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore), this)
        )[RegisterViewModel::class.java]
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun action() {
        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val phone = binding.phoneEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val passwordConfirm = binding.repeatPasswordEditText.text.toString()
            when {
                name.isEmpty() -> {
                    binding.nameEditTextLayout.error = resources.getString(R.string.empty_name)
                }
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = resources.getString(R.string.empty_email)
                }
                phone.isEmpty() -> {
                    binding.phoneEditText.error = resources.getString(R.string.phone_email)
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = resources.getString(R.string.empty_password)
                }
                passwordConfirm.isEmpty() -> {
                    binding.repeatPasswordEditText.error = resources.getString(R.string.empty_repeat_password)
                }
                passwordConfirm != password -> {
                    binding.repeatPasswordEditTextLayout.error = resources.getString(R.string.different_repeat_password)
                    binding.repeatPasswordEditText.text?.clear()
                }
                else -> {
                    val user = UserModel(email = email, name = name, phone = phone,
                        password = password, passwordConfirm = passwordConfirm)
                    registerViewModel.registerUser(user)

                    registerViewModel.toast.observe(this) {
                        showToast(it)

                        if(it == "Akun berhasil dibuat") {
                            val intent = Intent(this, VerificationActivity::class.java)
                            intent.putExtra(EXTRA_USER, user)
                            startActivity(intent)
                            finish()
                        }
                    }

                }
            }

        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbRegister.visibility = View.VISIBLE
        } else {
            binding.pbRegister.visibility = View.GONE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_USER = "EXTRA_USER"
    }

}