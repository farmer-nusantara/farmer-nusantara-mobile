package com.fahruaz.farmernusantara.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.fahruaz.farmernusantara.R
import com.fahruaz.farmernusantara.databinding.ActivityEditFarmlandBinding
import com.fahruaz.farmernusantara.response.farmland.ShowFarmlandDetailResponse
import com.fahruaz.farmernusantara.ui.fragment.farmland.FarmlandFragment

class EditFarmlandActivity : AppCompatActivity() {

    private var binding: ActivityEditFarmlandBinding? = null
    private var customProgressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditFarmlandBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // toolbar
        setSupportActionBar(binding?.tbEditFarmland)
        if(supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.tbEditFarmland?.setNavigationOnClickListener {
            onBackPressed()
        }

        val farmland = intent.getParcelableExtra<ShowFarmlandDetailResponse>(FarmlandFragment.EXTRA_FARMLAND)
        binding?.result = farmland
        val hexColorToInt = Color.parseColor(farmland?.markColor)
        binding?.previewSelectedColor?.setColorFilter(hexColorToInt)
        Glide.with(this@EditFarmlandActivity)
            .load(farmland?.imageUrl)
            .placeholder(R.drawable.image_default)
            .into(binding?.ivFarmland!!)


        FarmlandFragment.farmlandViewModel?.toastDeleteFarmland?.observe(this) {
            if(it.isNotEmpty()) {
                showToast(it)
                if(it == "Berhasil menghapus farmland") {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
            }
        }

        FarmlandFragment.farmlandViewModel?.toastDeleteFarmland?.observe(this) {
            if(it.isNotEmpty()) {
                showToast(it)
            }
        }

        FarmlandFragment.farmlandViewModel?.isLoading?.observe(this) {
            showLoading(it)
        }

        MainActivity.imageStorageViewModel.toast.observe(this) {
            if(it.isNotEmpty()) {
                showToast(it)
            }
        }

        MainActivity.imageStorageViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        MainActivity.imageStorageViewModel.imageUrl.observe(this) {
            if(it.isEmpty()) {
                FarmlandFragment.farmlandViewModel?.deleteFarmland(farmland?.id!!)
            }
        }

        binding?.btDeleteFarmland?.setOnClickListener {
            if(farmland?.imageUrl!! != "") {
                Log.e("ada", farmland.imageUrl)
                MainActivity.imageStorageViewModel.deleteImageFromStorage(farmland.imageUrl)
            }
            else {
                Log.e("tidak ada", "gambar")
                FarmlandFragment.farmlandViewModel?.deleteFarmland(farmland.id!!)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading)
            showProgressDialog()
        else
            cancelProgressDialog()
    }

    private fun showProgressDialog() {
        customProgressDialog = Dialog(this)
        customProgressDialog?.setContentView(R.layout.dialog_custom_progressbar)
        customProgressDialog?.show()
    }

    private fun cancelProgressDialog() {
        if (customProgressDialog != null) {
            customProgressDialog?.dismiss()
            customProgressDialog = null
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}