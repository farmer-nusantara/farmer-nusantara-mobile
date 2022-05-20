package com.fahruaz.farmernusantara.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fahruaz.farmernusantara.api.ApiConfig
import com.fahruaz.farmernusantara.response.farmland.CreateFarmlandResponse
import com.fahruaz.farmernusantara.response.file.UploadImageToStorageResponse
import com.fahruaz.farmernusantara.ui.MainActivity
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FarmlandViewModel : ViewModel() {

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String> = _toast

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun createFarmland(farmName: String, owner: String, markColor: String, plantType: String, location: String, imageUrl: String) {
        _isLoading.value = true
        val service = ApiConfig().getApiService().createFarmland("Token ${MainActivity.userModel?.token}", farmName, owner,
            markColor, plantType, location, imageUrl)

        service.enqueue(object : Callback<CreateFarmlandResponse> {
            override fun onResponse(call: Call<CreateFarmlandResponse>, response: Response<CreateFarmlandResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _toast.value = "Berhasil membuat farmland"
                    }
                }
                else
                    _toast.value = "Gagal membuat farmland"
            }
            override fun onFailure(call: Call<CreateFarmlandResponse>, t: Throwable) {
                _isLoading.value = false
                _toast.value = "Gagal instance Retrofit"
            }
        })
    }

}