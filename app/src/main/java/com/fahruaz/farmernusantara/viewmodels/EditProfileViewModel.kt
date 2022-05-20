package com.fahruaz.farmernusantara.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fahruaz.farmernusantara.api.ApiConfig
import com.fahruaz.farmernusantara.models.UserModel
import com.fahruaz.farmernusantara.preferences.UserPreferences
import com.fahruaz.farmernusantara.response.auth.ChangePasswordMessageResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileViewModel(private val pref: UserPreferences): ViewModel() {
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    private val _toast = MutableLiveData<String>()
//    val toast: LiveData<String> = _toast

//    fun editProfile(email: String, newPassword: String, passwordConfirmation: String) {
//        _isLoading.value = true
//        val service = ApiConfig().getApiService().changePasswordAccount(email, newPassword, passwordConfirmation)
//
//        service.enqueue(object : Callback<ChangePasswordMessageResponse> {
//            override fun onResponse(call: Call<ChangePasswordMessageResponse>, response: Response<ChangePasswordMessageResponse>) {
//                _isLoading.value = false
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    if (responseBody != null && responseBody.message == "Changed password is successfully") {
//                        _toast.value = "Berhasil mengganti kata sandi"
//                        changePassword(newPassword)
//                    }
//                }
//                else
//                    _toast.value = response.message()
//            }
//            override fun onFailure(call: Call<ChangePasswordMessageResponse>, t: Throwable) {
//                _isLoading.value = false
//                _toast.value = "Gagal instance Retrofit"
//            }
//        })
//    }

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }
}