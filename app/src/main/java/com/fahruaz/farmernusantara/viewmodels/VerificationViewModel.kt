package com.fahruaz.farmernusantara.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fahruaz.farmernusantara.api.ApiConfig
import com.fahruaz.farmernusantara.models.UserModel
import com.fahruaz.farmernusantara.preferences.UserPreferences
import com.fahruaz.farmernusantara.response.auth.ChangeStatusAccountMessageResponse
import com.fahruaz.farmernusantara.response.auth.RegisterMessageResponse
import com.fahruaz.farmernusantara.response.auth.SendTokenMessageResponse
import com.fahruaz.farmernusantara.response.auth.SendTokenResponse
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

        val service = ApiConfig().getApiService().sendToken(email)
        service.enqueue(object : Callback<SendTokenMessageResponse> {
            override fun onResponse(call: Call<SendTokenMessageResponse>, response: Response<SendTokenMessageResponse>) {
//                _isLoading.value = false
                Log.e("VerificationViewModel", "YOI")

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.message == "Send Token reset Successfully") {
                        _toast.value = "Token verifikasi berhasil dikirim"
                    }
                }
                else
                    _toast.value = response.message()
            }
            override fun onFailure(call: Call<SendTokenMessageResponse>, t: Throwable) {
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

}