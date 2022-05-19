package com.fahruaz.farmernusantara.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.fahruaz.farmernusantara.api.ApiConfig
import com.fahruaz.farmernusantara.models.UserModel
import com.fahruaz.farmernusantara.preferences.UserPreferences
import com.fahruaz.farmernusantara.response.auth.SendTokenActivationMessageResponse
import com.fahruaz.farmernusantara.response.auth.SignInMessageResponse
import com.fahruaz.farmernusantara.response.auth.TokenResetPasswordResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordViewModel(private val pref: UserPreferences): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String> = _toast

    fun sendCode(email: String) {
        _isLoading.value = true
        val service = ApiConfig().getApiService().sendTokenResetPassword(email)

        service.enqueue(object : Callback<TokenResetPasswordResponse> {
            override fun onResponse(call: Call<TokenResetPasswordResponse>, response: Response<TokenResetPasswordResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.message == "Send Token reset Successfully") {
                        Log.e("aku", "demon lord")
                        _toast.value = "Kode OTP berhasil dikirim"
                    }
                }
                else
                    _toast.value = response.message()
            }
            override fun onFailure(call: Call<TokenResetPasswordResponse>, t: Throwable) {
                _isLoading.value = false
                _toast.value = "Gagal instance Retrofit"
            }
        })
    }

    private fun changePassword(newPassword: String) {
        viewModelScope.launch {
            pref.changePassword(newPassword)
        }
    }

}