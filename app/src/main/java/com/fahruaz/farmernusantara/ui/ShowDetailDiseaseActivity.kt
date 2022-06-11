package com.fahruaz.farmernusantara.ui

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.fahruaz.farmernusantara.R
import com.fahruaz.farmernusantara.databinding.ActivityShowDetailDiseaseBinding
import com.fahruaz.farmernusantara.databindingadapters.ShowDetailDiseaseBinding
import com.fahruaz.farmernusantara.response.plantdisease.GetSickPlantResponse
import com.fahruaz.farmernusantara.ui.fragment.farmland.FarmlandFragment
import com.fahruaz.farmernusantara.viewmodels.ShowDetailDiseaseViewModel

class ShowDetailDiseaseActivity : AppCompatActivity() {

    private var binding: ActivityShowDetailDiseaseBinding? = null
    private lateinit var showDiseaseDetailViewModel: ShowDetailDiseaseViewModel
    private var customProgressDialog: Dialog? = null
    private lateinit var sickPlantDetail: GetSickPlantResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowDetailDiseaseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // toolbar
        setSupportActionBar(binding?.tbDetailDisease)
        if(supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.tbDetailDisease?.setNavigationOnClickListener {
            onBackPressed()
        }

        showDiseaseDetailViewModel = ViewModelProvider(this)[ShowDetailDiseaseViewModel::class.java]

        showDiseaseDetailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        showDiseaseDetailViewModel.disease.observe(this) {
            sickPlantDetail = it

            ShowDetailDiseaseBinding.setDiseaseNameDiseaseDetail(binding?.tvTitleDisease!!, it.diseasePlant!!)
            ShowDetailDiseaseBinding.setDateDiseaseDetail(binding?.tvDate!!, it.createdAt!!)
            ShowDetailDiseaseBinding.loadImageFromUrlDiseaseDetail(binding?.ivDisease!!, it.imageUrl!!)
            
            when(binding?.tvTitleDisease?.text?.trim().toString()) {
                "Common_Rust" -> {
                    binding?.tvTitleDisease?.text = resources.getString(R.string.Common_Rust)
                    binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Common_Rust)
                    binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Common_Rust)
                }
                "Northern_Leaf_Blight" -> {
                    binding?.tvTitleDisease?.text = resources.getString(R.string.Northern_Leaf_Blight)
                    binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Northern_Leaf_Blight)
                    binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Northern_Leaf_Blight)
                }
                "Cercospora_Leaf_Spot_Gray_Leaf_Spot" -> {
                    binding?.tvTitleDisease?.text = resources.getString(R.string.Cercospora_Leaf_Spot_Gray_Leaf_Spot)
                    binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Cercospora_Leaf_Spot_Gray_Leaf_Spot)
                    binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Cercospora_Leaf_Spot_Gray_Leaf_Spot)
                }
                "Bacterial Leaf Blight" -> {
                    binding?.tvTitleDisease?.text = resources.getString(R.string.Bacterial_Leaf_Blight)
                    binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Bacterial_Leaf_Blight)
                    binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Bacterial_Leaf_Blight)
                }
                "Bacterial Leaf Streak" -> {
                    binding?.tvTitleDisease?.text = resources.getString(R.string.Bacterial_Leaf_Streak)
                    binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Bacterial_Leaf_Streak)
                    binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Bacterial_Leaf_Streak)
                }
                "Bacterial Panicle Blight" -> {
                    binding?.tvTitleDisease?.text = resources.getString(R.string.Bacterial_Panicle_Blight)
                    binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Bacterial_Panicle_Blight)
                    binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Bacterial_Panicle_Blight)
                }
                "Blast" -> {
                    binding?.tvTitleDisease?.text = resources.getString(R.string.Blast)
                    binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Blast)
                    binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Blast)
                }
                "Brown Spot" -> {
                    binding?.tvTitleDisease?.text = resources.getString(R.string.Brown_Spot)
                    binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Brown_Spot)
                    binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Brown_Spot)
                }
                "Dead Heart" -> {
                    binding?.tvTitleDisease?.text = resources.getString(R.string.Dead_Heart)
                    binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Dead_Heart)
                    binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Dead_Heart)
                }
                "Down Mildew" -> {
                    binding?.tvTitleDisease?.text = resources.getString(R.string.Down_Mildew)
                    binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Down_Mildew)
                    binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Down_Mildew)
                }
                "Hispa" -> {
                    binding?.tvTitleDisease?.text = resources.getString(R.string.Hispa)
                    binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Hispa)
                    binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Hispa)
                }
                "Tungro" -> {
                    binding?.tvTitleDisease?.text = resources.getString(R.string.Tungro)
                    binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Tungro)
                    binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Tungro)
                }
                "angular_leaf_spot" -> {
                    binding?.tvTitleDisease?.text = resources.getString(R.string.angular_leaf_spot)
                    binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_angular_leaf_spot)
                    binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_angular_leaf_spot)
                }
                "bean_rust" -> {
                    binding?.tvTitleDisease?.text = resources.getString(R.string.bean_rust)
                    binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_bean_rust)
                    binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_bean_rust)
                }
                else -> {
                    binding?.tvTitleDisease?.text = resources.getString(R.string.healthy)
                    binding?.tvDescriptionDisease?.text = resources.getString(R.string.healthyPlant)
                    binding?.tvRecommendationCare?.text = resources.getString(R.string.empty)
                }
            }
        }

        val diseaseId = intent.getStringExtra(DetailFarmlandActivity.DISEASE_ID_EXTRA)
        val farmlandId = intent.getStringExtra(FarmlandFragment.EXTRA_FARMLAND_ID)
        if(diseaseId != null) {
            requestApiData(diseaseId)
        }

        showDiseaseDetailViewModel.toast.observe(this) {
            if(it.isNotEmpty()) {
                if(it == "Berhasil menghapus penyakit") {
                    showToast(it)
                    DetailFarmlandActivity.isSaveBtnClicked = true
                    finish()
                }
            }
        }

        MainActivity.imageStorageViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        MainActivity.imageStorageViewModel.toast.observe(this) {
            if(it.isNotEmpty()) {
                if(it != "Berhasil menghapus foto") {
                    showToast(it)
                }
                if(it == "Berhasil menghapus foto") {
                    showDiseaseDetailViewModel.deleteSickPlant(farmlandId!!, diseaseId!!)
                }
            }
        }

        binding?.deleteDataBtn?.setOnClickListener {
            showDeleteDialog()
        }
    }

    private fun requestApiData(diseaseId: String) {
        showDiseaseDetailViewModel.getSickPlant(diseaseId)
    }

    private fun showDeleteDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Hapus Penyakit")
        builder.setMessage("Apakah Anda yakin ingin menghapus penyakit ini?")
        builder.setPositiveButton("Hapus") { dialog, _ ->
            dialog.cancel()
            MainActivity.imageStorageViewModel.deleteImageFromStorage(sickPlantDetail.imageUrl!!)
        }
        builder.setNegativeButton("Tidak") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
        customProgressDialog?.setCancelable(false)
        customProgressDialog?.show()
    }

    private fun cancelProgressDialog() {
        if (customProgressDialog != null) {
            customProgressDialog?.dismiss()
            customProgressDialog = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}