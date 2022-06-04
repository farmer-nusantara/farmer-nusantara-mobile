package com.fahruaz.farmernusantara.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fahruaz.farmernusantara.api.ApiConfig
import com.fahruaz.farmernusantara.response.plantdisease.GetAllSickPlantsResponseItem
import com.fahruaz.farmernusantara.ui.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiseaseHistoryViewModel: ViewModel() {

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String> = _toast

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listDisease = MutableLiveData<List<GetAllSickPlantsResponseItem>>()
    val listDisease: LiveData<List<GetAllSickPlantsResponseItem>> = _listDisease

    fun getAllSickPlants(farmlandId: String) {
        _isLoading.value = true

        val service = ApiConfig().getApiService().getAllSickPlants("Token ${MainActivity.userModel?.token}", farmlandId)

        service.enqueue(object : Callback<List<GetAllSickPlantsResponseItem>> {
            override fun onResponse(call: Call<List<GetAllSickPlantsResponseItem>>, response: Response<List<GetAllSickPlantsResponseItem>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listDisease.value = responseBody.asReversed()
                        Log.e("pengen tidur", _listDisease.toString())
                    }
                }
                else {
                    _toast.value = "Gagal  mengambil data penyakit"
                    _toast.value = ""
                }
            }
            override fun onFailure(call: Call<List<GetAllSickPlantsResponseItem>>, t: Throwable) {
                _isLoading.value = false
                _toast.value = "Gagal instance Retrofit aja kan"
                _toast.value = ""
            }
        })
    }

}