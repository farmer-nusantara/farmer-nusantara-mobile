package com.fahruaz.farmernusantara.viewmodels

import androidx.lifecycle.*
import com.fahruaz.farmernusantara.api.ApiConfig
import com.fahruaz.farmernusantara.preferences.UserPreferences
import com.fahruaz.farmernusantara.response.auth.*
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
                        _toast.value = "Kode OTP berhasil dikirim"
                        _toast.value = ""
                    }
                }
                else {
                    _toast.value = response.message()
                    _toast.value = ""
                }
            }
            override fun onFailure(call: Call<TokenResetPasswordResponse>, t: Throwable) {
                _isLoading.value = false
                _toast.value = "Gagal instance Retrofit"
                _toast.value = ""
            }
        })
    }

    fun checkingTokenResetPassword(secretCode: Int) {
        _isLoading.value = true
        val service = ApiConfig().getApiService().checkingTokenResetPassword(secretCode)

        service.enqueue(object : Callback<CheckTokenResetMessageResponse> {
            override fun onResponse(call: Call<CheckTokenResetMessageResponse>, response: Response<CheckTokenResetMessageResponse>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.message == "Successfully") {
                        _toast.value = "Kode OTP benar"
                        _toast.value = ""
                    }
                }
                else {
                    _toast.value = response.message()
                    _toast.value = ""
                }
            }
            override fun onFailure(call: Call<CheckTokenResetMessageResponse>, t: Throwable) {
                _isLoading.value = false
                _toast.value = "Gagal instance Retrofit"
                _toast.value = ""
            }
        })
    }

    fun changePasswordAccount(email: String, newPassword: String, passwordConfirmation: String) {
        _isLoading.value = true
        val service = ApiConfig().getApiService().changePasswordAccount(email, newPassword, passwordConfirmation)

        service.enqueue(object : Callback<ChangePasswordMessageResponse> {
            override fun onResponse(call: Call<ChangePasswordMessageResponse>, response: Response<ChangePasswordMessageResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.message == "Changed password is successfully") {
                        _toast.value = "Berhasil mengganti kata sandi"
                        _toast.value = ""
                        changePassword(newPassword)
                    }
                }
                else {
                    _toast.value = response.message()
                    _toast.value = ""
                }
            }
            override fun onFailure(call: Call<ChangePasswordMessageResponse>, t: Throwable) {
                _isLoading.value = false
                _toast.value = "Gagal instance Retrofit"
                _toast.value = ""
            }
        })
    }

    private fun changePassword(newPassword: String) {
        viewModelScope.launch {
            pref.changePassword(newPassword)
            pref.logout()
        }
    }

}