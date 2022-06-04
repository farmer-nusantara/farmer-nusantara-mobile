package com.fahruaz.farmernusantara.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fahruaz.farmernusantara.api.ApiConfig
import com.fahruaz.farmernusantara.response.farmland.ShowFarmlandDetailResponse
import com.fahruaz.farmernusantara.response.plantdisease.GetSickPlantResponse
import com.fahruaz.farmernusantara.ui.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowDetailDiseaseViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String> = _toast

    private val _disease = MutableLiveData<GetSickPlantResponse>()
    val disease: LiveData<GetSickPlantResponse> = _disease

    fun getSickPlant(id: String) {
        _isLoading.value = true

        val service = ApiConfig().getApiService().GetSickPlant("Token ${MainActivity.userModel?.token}", id)

        service.enqueue(object : Callback<GetSickPlantResponse> {
            override fun onResponse(call: Call<GetSickPlantResponse>, response: Response<GetSickPlantResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _disease.value = responseBody
                    }
                }
                else {
                    _toast.value = "Gagal mendapatkan detail penyakit"
                    _toast.value = ""
                }
            }
            override fun onFailure(call: Call<GetSickPlantResponse>, t: Throwable) {
                _isLoading.value = false
                _toast.value = "Gagal instance Retrofit"
                _toast.value = ""
            }
        })
    }

}