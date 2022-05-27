package com.fahruaz.farmernusantara.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fahruaz.farmernusantara.api.ApiConfig
import com.fahruaz.farmernusantara.models.UserModel
import com.fahruaz.farmernusantara.preferences.UserPreferences
import com.fahruaz.farmernusantara.response.farmland.CreateFarmlandResponse
import com.fahruaz.farmernusantara.response.farmland.GetAllFarmlandByOwnerResponseItem
import com.fahruaz.farmernusantara.ui.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FarmlandViewModel(private val pref: UserPreferences) : ViewModel() {

    private val _toastFarmland = MutableLiveData<String>()
    val toastFarmland: LiveData<String> = _toastFarmland

    //    private val _toastCreateFarmland = MutableLiveData<String>()
    val toastCreateFarmland: MutableLiveData<String> = MutableLiveData<String>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listFarmland = MutableLiveData<List<GetAllFarmlandByOwnerResponseItem>>()
    val listFarmland: LiveData<List<GetAllFarmlandByOwnerResponseItem>> = _listFarmland

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun getAllFarmlandByOwner(owner: String, token: String) {
        _isLoading.value = true

        val service = ApiConfig().getApiService().getAllFarmlandByOwner("Token $token", owner)

        service.enqueue(object : Callback<List<GetAllFarmlandByOwnerResponseItem>> {
            override fun onResponse(call: Call<List<GetAllFarmlandByOwnerResponseItem>>, response: Response<List<GetAllFarmlandByOwnerResponseItem>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _toastFarmland.value = "Berhasil mengambil data farmland"
                        _toastFarmland.value = ""
                        _listFarmland.value = responseBody.asReversed()
                    }
                }
                else {
                    _toastFarmland.value = "Gagal  mengambil data farmland"
                }
            }
            override fun onFailure(call: Call<List<GetAllFarmlandByOwnerResponseItem>>, t: Throwable) {
                _isLoading.value = false
                _toastFarmland.value = "Gagal instance Retrofit aja kan"
            }
        })
    }

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
                        toastCreateFarmland.value = "Berhasil membuat farmland"
                        toastCreateFarmland.value = ""
                    }
                }
                else {
                    toastCreateFarmland.value = "Gagal membuat farmland. Coba ganti nama atau warna farmland"
                }
            }
            override fun onFailure(call: Call<CreateFarmlandResponse>, t: Throwable) {
                _isLoading.value = false
                toastCreateFarmland.value = "Gagal instance Retrofit"
            }
        })
    }

}