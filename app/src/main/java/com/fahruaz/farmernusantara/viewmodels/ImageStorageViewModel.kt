package com.fahruaz.farmernusantara.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fahruaz.farmernusantara.api.ApiConfig
import com.fahruaz.farmernusantara.response.file.UploadImageToStorageResponse
import com.fahruaz.farmernusantara.ui.CreateFarmlandActivity
import com.fahruaz.farmernusantara.ui.MainActivity
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImageStorageViewModel : ViewModel() {

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String> = _toast

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String> = _imageUrl

    fun uploadImageToStorage(id: String, image: MultipartBody.Part) {
        _isLoading.value = true
        val service = ApiConfig().getApiService().uploadImageToStorage("Token ${MainActivity.userModel?.token}", id, image)

        service.enqueue(object : Callback<UploadImageToStorageResponse> {
            override fun onResponse(call: Call<UploadImageToStorageResponse>, response: Response<UploadImageToStorageResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _toast.value = "Berhasil mengunggah foto"
                        _imageUrl.value = responseBody.imageUrl
                    }
                }
                else
                    _toast.value = response.message()
            }
            override fun onFailure(call: Call<UploadImageToStorageResponse>, t: Throwable) {
                _isLoading.value = false
                _toast.value = "Gagal instance Retrofit"
            }
        })
    }

}