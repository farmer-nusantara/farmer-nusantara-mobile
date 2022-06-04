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

    fun getAllDiseases(token: String,  id: String) {
        val service = ApiConfig().getApiService().getAllSickPlants(token, id)

        service.enqueue(object : Callback<List<GetAllSickPlantsResponseItem>> {
            //            override fun onResponse(call: Call<GetAllSickPlantsResponse>,
//                                    response: Response<GetAllSickPlantsResponse>) {
//                Log.e("asdewf", "sfiejfe")
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    if (responseBody != null) {
//                        _listDiseases.value = responseBody.getAllSickPlantsResponse as List<GetAllSickPlantsResponseItem>
//                    }
//                }
//            }
//            override fun onFailure(call: Call<GetAllSickPlantsResponse>, t: Throwable) {
//                //DO NOTHING
//                t.printStackTrace();
//            }
            override fun onResponse(
                call: Call<List<GetAllSickPlantsResponseItem>>,
                response: Response<List<GetAllSickPlantsResponseItem>>
            ) {
//                Log.e("asu", "hahaha")
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listDiseases.value = responseBody
                    }
                }
            }
            override fun onFailure(call: Call<List<GetAllSickPlantsResponseItem>>, t: Throwable) {
                Log.e("asu", t.message.toString())

            }
        })
    }
}