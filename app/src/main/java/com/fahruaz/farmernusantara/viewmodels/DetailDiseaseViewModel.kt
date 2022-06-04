package com.fahruaz.farmernusantara.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fahruaz.farmernusantara.api.ApiConfig
import com.fahruaz.farmernusantara.response.farmland.CreateFarmlandResponse
import com.fahruaz.farmernusantara.response.plantdisease.SaveDiseasePlantResponse
import com.fahruaz.farmernusantara.ui.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailDiseaseViewModel: ViewModel() {

    val toast: MutableLiveData<String> = MutableLiveData<String>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun saveDiseasePlant(farmlandId: String, latitude: Double, longitude: Double, diseasePlant: String, imageUrl: String, picturedBy: String) {
        _isLoading.value = true

        val service = ApiConfig().getApiService().saveDiseasePlant("Token ${MainActivity.userModel?.token}", farmlandId, latitude,
            longitude, diseasePlant, imageUrl, picturedBy)

        service.enqueue(object : Callback<SaveDiseasePlantResponse> {
            override fun onResponse(call: Call<SaveDiseasePlantResponse>, response: Response<SaveDiseasePlantResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        toast.value = "Berhasil menyimpan penyakit"
                        toast.value = ""
                    }
                }
                else {
                    toast.value = "Gagal menyimpan penyakit"
                    toast.value = ""
                }
            }
            override fun onFailure(call: Call<SaveDiseasePlantResponse>, t: Throwable) {
                _isLoading.value = false
                toast.value = "Gagal instance Retrofit"
                toast.value = ""
            }
        })
    }

}