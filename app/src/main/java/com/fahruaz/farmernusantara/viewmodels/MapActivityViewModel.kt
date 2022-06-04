package com.fahruaz.farmernusantara.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fahruaz.farmernusantara.api.ApiConfig
import com.fahruaz.farmernusantara.response.plantdisease.GetAllSickPlantsResponse
import com.fahruaz.farmernusantara.response.plantdisease.GetAllSickPlantsResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapActivityViewModel: ViewModel() {
    private val _listDiseases = MutableLiveData<List<GetAllSickPlantsResponseItem>>()
    val listDiseases: LiveData<List<GetAllSickPlantsResponseItem>> = _listDiseases

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String> = _toast

    fun getAllDiseases(token: String,  id: String) {
        _isLoading.value = true
        val service = ApiConfig().getApiService().getAllSickPlants(token, id)

        service.enqueue(object : Callback<List<GetAllSickPlantsResponseItem>> {
            override fun onResponse(
                call: Call<List<GetAllSickPlantsResponseItem>>,
                response: Response<List<GetAllSickPlantsResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listDiseases.value = responseBody
                    }
                }else
                    _toast.value = response.message()
            }
            override fun onFailure(call: Call<List<GetAllSickPlantsResponseItem>>, t: Throwable) {
                _isLoading.value  = false
                _toast.value = t.message
            }
        })
    }
}