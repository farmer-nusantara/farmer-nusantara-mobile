package com.fahruaz.farmernusantara.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.fahruaz.farmernusantara.databinding.ActivityLoginBinding
import com.fahruaz.farmernusantara.models.UserModel
import com.fahruaz.farmernusantara.preferences.UserPreferences
import com.fahruaz.farmernusantara.viewmodels.LoginViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var user: UserModel
    private var customProgressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        loginViewModel.toast.observe(this) {
            showToast(it)
        }

        binding.forgotPassword.setOnClickListener {
            val intent = Intent(this, ChangePasswordEmailActivity::class.java)
            startActivity(intent)
        }

        setupView()
        action()
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

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore), this)
        )[LoginViewModel::class.java]

        loginViewModel.getUser().observe(this) { user ->
            this.user = user
            if (user.token?.isNotEmpty() == true){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun action() {
        binding.loginButton.setOnClickListener {
            binding.emailEditTextLayout.error = ""
            binding.passwordEditTextLayout.error = ""

            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = resources.getString(R.string.empty_email)
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.emailEditTextLayout.error = resources.getString(R.string.invalid_email)
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = resources.getString(R.string.empty_password)
                }
                password.length < 8 -> {
                    binding.passwordEditTextLayout.error = resources.getString(R.string.password_lacking)
                }
                else -> {
                    val user = UserModel(email = email, password = password)

//                     loginViewModel.name.observe(this) {
//                         user.name = it
//                     }
//                     loginViewModel.token.observe(this) {
//                         user.token = it
//                         token = it
//                     }
//                     loginViewModel.phone.observe(this) {
//                         user.phone = it
//                     }
//                     loginViewModel.status.observe(this) {
//                         user.status = it
//                     }
//                     loginViewModel.id.observe(this) {
//                         user.id = it
//                     }

                    loginViewModel.loginUser(user)

                    loginViewModel.toast.observe(this) {
                        if (it == "Login successfully") {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }

        binding.daftarText.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
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

    companion object {
        var token = ""
        lateinit var loginViewModel: LoginViewModel
    }
}