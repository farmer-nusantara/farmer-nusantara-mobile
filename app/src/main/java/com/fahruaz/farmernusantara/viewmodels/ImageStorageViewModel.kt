package com.fahruaz.farmernusantara.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fahruaz.farmernusantara.api.ApiConfig
import com.fahruaz.farmernusantara.response.file.DeleteImageFromStorageResponse
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

    private val _imageEditUrl = MutableLiveData<String>()
    val imageEditUrl: LiveData<String> = _imageEditUrl

    fun uploadImageToStorage(id: String, image: MultipartBody.Part) {
        _isLoading.value = true
        _toast.value = ""
        val service = ApiConfig().getApiService().uploadImageToStorage("Token ${MainActivity.userModel?.token}", id, image)

        service.enqueue(object : Callback<UploadImageToStorageResponse> {
            override fun onResponse(call: Call<UploadImageToStorageResponse>, response: Response<UploadImageToStorageResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _toast.value = "Berhasil mengunggah foto"
                        _toast.value = ""
                        _imageUrl.value = responseBody.imageUrl
                        _imageEditUrl.value = responseBody.imageUrl
                        _imageUrl.value = ""
                    }
                }
                else {
                    _toast.value = "Gagal mengunggah foto"
                    _toast.value = ""
                }
            }
            override fun onFailure(call: Call<UploadImageToStorageResponse>, t: Throwable) {
                _isLoading.value = false
                _toast.value = "Gagal instance Retrofit"
                _toast.value = ""
            }
        })
    }

    fun deleteImageFromStorage(imageUrl: String) {
        _isLoading.value = true
        val service = ApiConfig().getApiService().deleteImageFromStorage("Token ${MainActivity.userModel?.token}", imageUrl)

        service.enqueue(object : Callback<DeleteImageFromStorageResponse> {
            override fun onResponse(call: Call<DeleteImageFromStorageResponse>, response: Response<DeleteImageFromStorageResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _toast.value = "Berhasil menghapus foto"
                        _toast.value = ""
                        _imageUrl.value = ""
                        _imageEditUrl.value = ""
                    }
                }
                else {
                    _toast.value = "Gagal menghapus foto"
                    _toast.value = ""
                }
            }
            override fun onFailure(call: Call<DeleteImageFromStorageResponse>, t: Throwable) {
                _isLoading.value = false
                _toast.value = "Gagal instance Retrofit"
                _toast.value = "Gagal menghapus foto"
                _toast.value = ""
            }
        })
    }

}