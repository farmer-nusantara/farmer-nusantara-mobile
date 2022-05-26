package com.fahruaz.farmernusantara.viewmodels

import android.util.Log
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

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String> = _toast

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
                        _toast.value = "Berhasil mengambil data farmland"
                        _listFarmland.value = responseBody.asReversed()
                        Log.e("getAllFarmlandByOwner", _listFarmland.toString())
                    }
                }
                else {
                    _toast.value = "Gagal  mengambil data farmland"
                }
            }
            override fun onFailure(call: Call<List<GetAllFarmlandByOwnerResponseItem>>, t: Throwable) {
                _isLoading.value = false
                _toast.value = "Gagal instance Retrofit aja kan"
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