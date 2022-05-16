package com.fahruaz.farmernusantara.viewmodels

import androidx.lifecycle.*
import com.fahruaz.farmernusantara.api.ApiConfig
import com.fahruaz.farmernusantara.models.UserModel
import com.fahruaz.farmernusantara.preferences.UserPreferences
import com.fahruaz.farmernusantara.response.auth.SignInMessageResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreferences) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String> = _toast

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _token = MutableLiveData<String>()
    val token: LiveData<String> = _token

    fun loginUser(user: UserModel) {
        _isLoading.value = true
        val service = ApiConfig().getApiService().loginUser(user.email!!, user.password!!)

        service.enqueue(object : Callback<SignInMessageResponse> {
            override fun onResponse(call: Call<SignInMessageResponse>, response: Response<SignInMessageResponse>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.message == "Login successfully") {
                        _toast.value = "Berhasil masuk"
                        _name.value = responseBody.user?.name!!
                        _token.value = responseBody.token!!
                        signin(user)
                    }
                }
                else
                    _toast.value = response.message()
            }
            override fun onFailure(call: Call<SignInMessageResponse>, t: Throwable) {
                _isLoading.value = false
                _toast.value = "Gagal instance Retrofit"
            }
        })
    }

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    private fun signin(user: UserModel) {
        viewModelScope.launch {
            pref.login(user)
        }
    }
}