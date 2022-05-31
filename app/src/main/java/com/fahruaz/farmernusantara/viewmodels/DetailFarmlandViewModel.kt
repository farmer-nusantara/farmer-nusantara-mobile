package com.fahruaz.farmernusantara.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fahruaz.farmernusantara.api.ApiConfig
import com.fahruaz.farmernusantara.response.farmland.GetAllFarmlandByOwnerResponseItem
import com.fahruaz.farmernusantara.response.farmland.ShowFarmlandDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailFarmlandViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String> = _toast

    private val _farmland = MutableLiveData<ShowFarmlandDetailResponse>()
    val farmland: LiveData<ShowFarmlandDetailResponse> = _farmland

    fun getAllFarmlandByOwner(id: String, token: String) {
        _isLoading.value = true

        val service = ApiConfig().getApiService().showFarmlandDetail("Token $token", id)

        service.enqueue(object : Callback<ShowFarmlandDetailResponse> {
            override fun onResponse(call: Call<ShowFarmlandDetailResponse>, response: Response<ShowFarmlandDetailResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _farmland.value = responseBody
                        _toast.value = "Berhasil mendapatkan detail farmland"
                        _toast.value = ""
                    }
                }
                else {
                    _toast.value = "Gagal mendapatkan detail farmland"
                    _toast.value = ""
                }
            }
            override fun onFailure(call: Call<ShowFarmlandDetailResponse>, t: Throwable) {
                _isLoading.value = false
                _toast.value = "Gagal instance Retrofit"
                _toast.value = ""
            }
        })
    }

}