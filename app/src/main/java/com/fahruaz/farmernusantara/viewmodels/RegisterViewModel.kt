package com.fahruaz.farmernusantara.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fahruaz.farmernusantara.api.ApiConfig
import com.fahruaz.farmernusantara.models.UserModel
import com.fahruaz.farmernusantara.preferences.UserPreferences
import com.fahruaz.farmernusantara.response.auth.RegisterMessageResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val pref: UserPreferences): ViewModel() {

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String> = _toast

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun registerUser(user: UserModel) {
        _isLoading.value = true

        val service = ApiConfig().getApiService().registerUser(user.email!!, user.name!!,
            user.phone!!, user.password!!, user.passwordConfirm!!)

        service.enqueue(object : Callback<RegisterMessageResponse> {
            override fun onResponse(call: Call<RegisterMessageResponse>, response: Response<RegisterMessageResponse>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.message == "Successfully") {
                       _toast.value = "Akun berhasil dibuat"
                        user.id = responseBody.data?.userId
                        setUser(user)
                    }
                }
                else
                    _toast.value = response.message()
            }
            override fun onFailure(call: Call<RegisterMessageResponse>, t: Throwable) {
                _isLoading.value = false
                _toast.value = "Gagal instance Retrofit"
            }
        })
    }

    fun setUser(user: UserModel){
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

}