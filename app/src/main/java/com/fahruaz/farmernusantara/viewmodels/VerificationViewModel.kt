package com.fahruaz.farmernusantara.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.fahruaz.farmernusantara.api.ApiConfig
import com.fahruaz.farmernusantara.models.UserModel
import com.fahruaz.farmernusantara.preferences.UserPreferences
import com.fahruaz.farmernusantara.response.auth.ChangeStatusAccountMessageResponse
import com.fahruaz.farmernusantara.response.auth.SendTokenActivationMessageResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerificationViewModel(private val pref: UserPreferences): ViewModel() {

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String> = _toast

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun sendToken(email: String) {
//        _isLoading.value = true

        val service = ApiConfig().getApiService().sendTokenActivationAccount(email)
        service.enqueue(object : Callback<SendTokenActivationMessageResponse> {
            override fun onResponse(call: Call<SendTokenActivationMessageResponse>, response: Response<SendTokenActivationMessageResponse>) {
//                _isLoading.value = false
                Log.e("VerificationViewModel", "YOI")

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _toast.value = "Token verifikasi berhasil dikirim"
                    }
                }
                else
                    _toast.value = response.message()
            }
            override fun onFailure(call: Call<SendTokenActivationMessageResponse>, t: Throwable) {
//                _isLoading.value = false
                _toast.value = "Gagal instance Retrofit"
            }
        })
    }

    fun changeStatusAccount(secretCode: Int) {
        _isLoading.value = true

        val service = ApiConfig().getApiService().changeStatusAccount(secretCode)
        service.enqueue(object : Callback<ChangeStatusAccountMessageResponse> {
            override fun onResponse(call: Call<ChangeStatusAccountMessageResponse>, response: Response<ChangeStatusAccountMessageResponse>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.message == "Successfully") {
                        _toast.value = "Berhasil verifikasi akun"
                        changeStatusAccount()
                    }
                }
                else
                    _toast.value = response.message()
            }
            override fun onFailure(call: Call<ChangeStatusAccountMessageResponse>, t: Throwable) {
                _isLoading.value = false
                _toast.value = "Gagal instance Retrofit"
            }
        })
    }

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun changeStatusAccount() {
        viewModelScope.launch {
            pref.changeStatusAccount()
        }
    }

}