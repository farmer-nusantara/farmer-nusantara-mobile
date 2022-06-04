package com.fahruaz.farmernusantara.ui

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.fahruaz.farmernusantara.R
import com.fahruaz.farmernusantara.databinding.ActivityShowDetailDiseaseBinding
import com.fahruaz.farmernusantara.databindingadapters.ShowDetailDiseaseBinding
import com.fahruaz.farmernusantara.viewmodels.ShowDetailDiseaseViewModel

class ShowDetailDiseaseActivity : AppCompatActivity() {

    private var binding: ActivityShowDetailDiseaseBinding? = null
    private lateinit var showDiseaseDetailViewModel: ShowDetailDiseaseViewModel
    private var customProgressDialog: Dialog? = null

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
            ShowDetailDiseaseBinding.setDiseaseNameDiseaseDetail(binding?.tvTitleDisease!!, it.diseasePlant!!)
            ShowDetailDiseaseBinding.setDateDiseaseDetail(binding?.tvDate!!, it.createdAt!!)
            ShowDetailDiseaseBinding.loadImageFromUrlDiseaseDetail(binding?.ivDisease!!, it.imageUrl!!)
            
            when(binding?.tvTitleDisease?.text?.trim().toString()) {
                "Common_Rust" -> {
                    binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Common_Rust)
                    binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Common_Rust)
                }
                "Northern_Leaf_Blight" -> {
                    binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Northern_Leaf_Blight)
                    binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Northern_Leaf_Blight)
                }
                "Cercospora_Leaf_Spot_Gray_Leaf_Spot" -> {
                    binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Cercospora_Leaf_Spot_Gray_Leaf_Spot)
                    binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Cercospora_Leaf_Spot_Gray_Leaf_Spot)
                }
                "Bacterial Leaf Blight" -> {
                    binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Bacterial_Leaf_Blight)
                    binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Bacterial_Leaf_Blight)
                }
                "Bacterial Leaf Streak" -> {
                    binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Bacterial_Leaf_Streak)
                    binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Bacterial_Leaf_Streak)
                }
                "Bacterial Panicle Blight" -> {
                    binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Bacterial_Panicle_Blight)
                    binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Bacterial_Panicle_Blight)
                }
                "Blast" -> {
                    binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Blast)
                    binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Blast)
                }
                "Brown Spot" -> {
                    binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Brown_Spot)
                    binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Brown_Spot)
                }
                "Dead Heart" -> {
                    binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Dead_Heart)
                    binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Dead_Heart)
                }
                "Down Mildew" -> {
                    binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Down_Mildew)
                    binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Down_Mildew)
                }
                "Hispa" -> {
                    binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Hispa)
                    binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Hispa)
                }
                "Tungro" -> {
                    binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Tungro)
                    binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Tungro)
                }
                else -> {
                    binding?.tvDescriptionDisease?.text = resources.getString(R.string.healthyPlant)
                    binding?.tvRecommendationCare?.text = resources.getString(R.string.empty)
                }
            }
        }

        val diseaseId = intent.getStringExtra(DetailFarmlandActivity.DISEASE_ID_EXTRA)
        if(diseaseId != null) {
            requestApiData(diseaseId)
        }
    }

    private fun requestApiData(diseaseId: String) {
        showDiseaseDetailViewModel.getSickPlant(diseaseId)
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

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}