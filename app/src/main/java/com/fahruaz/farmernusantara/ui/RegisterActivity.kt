package com.fahruaz.farmernusantara.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
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
import com.fahruaz.farmernusantara.viewmodels.LoginViewModel
import com.fahruaz.farmernusantara.viewmodels.RegisterViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
//    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userModel: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRegisterViewModel()
//        setupLoginViewModel()

        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        registerViewModel.toast.observe(this) {
            if(it !== "Berhasil masuk") {
                showToast(it)
                if(it == "Akun berhasil dibuat") {
                    val intent = Intent(this, VerificationActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        action()
        setupView()
    }

    private fun setupRegisterViewModel() {
        registerViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore), this)
        )[RegisterViewModel::class.java]
    }

//    private fun setupLoginViewModel() {
//        loginViewModel = ViewModelProvider(
//            this,
//            ViewModelFactory(UserPreferences.getInstance(dataStore), this)
//        )[LoginViewModel::class.java]
//    }

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
            binding.nameEditTextLayout.error = ""
            binding.emailEditTextLayout.error = ""
            binding.phoneEditTextLayout.error = ""
            binding.passwordEditTextLayout.error = ""
            binding.repeatPasswordEditTextLayout.error = ""

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
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.emailEditTextLayout.error = resources.getString(R.string.invalid_email)
                }
                phone.isEmpty() -> {
                    binding.phoneEditTextLayout.error = resources.getString(R.string.phone_email)
                }
                phone.length < 10 -> {
                    binding.phoneEditTextLayout.error = resources.getString(R.string.invalid_phone)
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = resources.getString(R.string.empty_password)
                }
                password.length < 8 -> {
                    binding.passwordEditTextLayout.error = resources.getString(R.string.password_lacking)
                }
                passwordConfirm.isEmpty() -> {
                    binding.repeatPasswordEditText.error = resources.getString(R.string.empty_repeat_password)
                }
                passwordConfirm != password -> {
                    binding.repeatPasswordEditTextLayout.error = resources.getString(R.string.different_repeat_password)
                    binding.repeatPasswordEditText.text?.clear()
                }
                else -> {
                    userModel = UserModel(email = email, name = name, phone = phone,
                        password = password, passwordConfirm = passwordConfirm)
                    registerViewModel.registerUser(userModel)

//                    LoginActivity.loginViewModel.token.observe(this) { token ->
//                        userModel.token = token
//                        LoginActivity.token = token
//                    }
//                    LoginActivity.loginViewModel.status.observe(this) {
//                        userModel.status = it
//                    }
                    LoginActivity.loginViewModel.loginUser(userModel)
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

}