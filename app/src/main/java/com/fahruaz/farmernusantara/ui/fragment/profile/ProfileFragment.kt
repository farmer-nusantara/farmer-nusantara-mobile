package com.fahruaz.farmernusantara.ui.fragment.profile

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.fahruaz.farmernusantara.R
import com.fahruaz.farmernusantara.api.ApiConfig
import com.fahruaz.farmernusantara.databinding.FragmentProfileBinding
import com.fahruaz.farmernusantara.response.profile.GetProfileResponse
import com.fahruaz.farmernusantara.ui.EditProfileActivity
import com.fahruaz.farmernusantara.ui.MainActivity
import com.fahruaz.farmernusantara.ui.fragment.farmland.FarmlandFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var user: GetProfileResponse
    private var customProgressDialog: Dialog? = null

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
            AlertDialog.Builder(requireContext()).apply {
                setTitle(resources.getString(R.string.logout))
                setMessage(resources.getString(R.string.sure_to_logout))
                setPositiveButton(resources.getString(R.string.ya)) { _, _ ->
                    MainActivity.mainViewModel?.logout()
                    FarmlandFragment.requestApi = true
                }
                setNegativeButton(resources.getString(R.string.back)) { _, _ ->}
                create()
                show()
            }
        }


        activity?.findViewById<FloatingActionButton>(R.id.fabFarmland)?.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_farmlandFragment)
        }

        getUser(MainActivity.userModel?.id!!)

        FarmlandFragment.requestApi = false
    }

    override fun onResume() {
        super.onResume()

        if(isEditBtnClicked) {
            getUser(MainActivity.userModel?.id!!)
            isEditBtnClicked = false
        }
    }

    private fun getUser(id: String) {

        showLoading(true)
        val client = ApiConfig().getApiService().getUserData("Token ${MainActivity.userModel?.token}",id)

        client.enqueue(object : Callback<GetProfileResponse> {
            override fun onResponse(
                call: Call<GetProfileResponse>,
                response: Response<GetProfileResponse>
            ) {
                if (response.isSuccessful) {
                    showLoading(false)
                    val responseBody = response.body()
                    if (responseBody != null) {
                        user = responseBody

                        binding.tvName.text = user.name
                        binding.tvEmail.text = user.email
                        binding.tvPhone.text = user.phone
                    }
                } else {
                    showLoading(false)
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GetProfileResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "Terjadi Kesalahan: ${t.message}")
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        Log.e("isloading", isLoading.toString())
        if (isLoading)
            showProgressDialog()
        else
            cancelProgressDialog()
    }

    private fun showProgressDialog() {
        customProgressDialog = Dialog(requireContext())
        customProgressDialog?.setContentView(R.layout.dialog_custom_progressbar)
        customProgressDialog?.setCancelable(false)
        customProgressDialog?.show()
    }

    private fun cancelProgressDialog() {
        if (customProgressDialog != null) {
            customProgressDialog?.dismiss()
            customProgressDialog = null
        }
    }

    companion object {
        private const val TAG = "DetailUser"
        var isEditBtnClicked = false
    }

}