package com.fahruaz.farmernusantara.ui.fragment.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.fahruaz.farmernusantara.R
import com.fahruaz.farmernusantara.api.ApiConfig
import com.fahruaz.farmernusantara.databinding.FragmentProfileBinding
import com.fahruaz.farmernusantara.models.UserModel
import com.fahruaz.farmernusantara.preferences.UserPreferences
import com.fahruaz.farmernusantara.response.profile.GetProfileResponse
import com.fahruaz.farmernusantara.ui.EditProfileActivity
import com.fahruaz.farmernusantara.ui.MainActivity
import com.fahruaz.farmernusantara.ui.RegisterActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var user: GetProfileResponse

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btLogout.setOnClickListener {
            val intent = Intent(requireContext(), RegisterActivity::class.java)
            startActivity(intent)
        }

        activity?.findViewById<FloatingActionButton>(R.id.fabFarmland)?.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_farmlandFragment)
        }

        Log.e("ProfileFragment", "ada")

        getUser(MainActivity.userModel?.id!!)
    }

    private fun getUser(id: String) {

        val client = ApiConfig().getApiService().getUserData("Token ${MainActivity.userModel?.token}",id)

        client.enqueue(object : Callback<GetProfileResponse> {
            override fun onResponse(
                call: Call<GetProfileResponse>,
                response: Response<GetProfileResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        user = responseBody


//                        Glide.with(this@ProfileFragment)
//                            .load(user.avatarUrl) // URL Gambar
//                            .circleCrop() // Mengubah image menjadi lingkaran
//                            .into(binding!!.imgItemPhoto) // imageView mana yang akan diterapkan
                        binding?.tvName?.text = user.name
                        binding?.tvEmail?.text = user.email
                        binding?.tvPhone?.text = user.phone
//                        binding?.tvLocation?.text = resources.getString(R.string.vLocation, user.location)
//                        binding?.tvRepo?.text = resources.getString(R.string.vRepository, user.publicRepos)
//                        binding?.tvFollowers?.text = resources.getString(R.string.vFollowers, user.followers)
//                        binding?.tvFollowing?.text = resources.getString(R.string.vFollowing, user.following)

//                        favoriteUser = UserEntity(username, user.name, user.location, user.company, user.publicRepos,
//                            user.avatarUrl, user.type)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GetProfileResponse>, t: Throwable) {
                //showLoading(false)
                Log.e(TAG, "Terjadi Kesalahan: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "DetailUser"
    }
    
}