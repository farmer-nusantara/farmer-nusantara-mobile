package com.fahruaz.farmernusantara.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fahruaz.farmernusantara.api.ApiConfig
import com.fahruaz.farmernusantara.response.farmland.CreateFarmlandResponse
import com.fahruaz.farmernusantara.ui.CreateFarmlandActivity
import com.fahruaz.farmernusantara.ui.MainActivity
import com.fahruaz.farmernusantara.util.reduceFileImage
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class FarmlandViewModel : ViewModel() {

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String> = _toast

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private fun uploadImageToStorage(owner: String, image: MultipartBody.Part) {
        runBlocking {
            val responseData = ApiConfig().getApiService().uploadImageToStorage2("Token ${MainActivity.userModel?.token}", owner, image)
            CreateFarmlandActivity.imageUrl = responseData.imageUrl!!
        }
    }

    fun createFarmland(farmName: String, owner: String, markColor: String, plantType: String, location: String, image: File?, imageUrl: String) {
        _isLoading.value = true

//        if(image != null) {
//            val file = reduceFileImage(image)
//            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
//            val imageMultipart = MultipartBody.Part.createFormData("file", file.name, requestImageFile)
//            uploadImageToStorage(owner, imageMultipart)
//        }

        Log.e("createFarmland", CreateFarmlandActivity.imageUrl)
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
                else {
                    _toast.value = "Gagal membuat farmland. Coba ganti nama atau warna farmland."
                }
            }
            override fun onFailure(call: Call<CreateFarmlandResponse>, t: Throwable) {
                _isLoading.value = false
                _toast.value = "Gagal instance Retrofit"
            }
        })
    }

}