package com.fahruaz.farmernusantara.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fahruaz.farmernusantara.databinding.ActivityDetailDiseaseBinding

class DetailDiseaseActivity : AppCompatActivity() {

    private var binding: ActivityDetailDiseaseBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDiseaseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // toolbar
        setSupportActionBar(binding?.tbDetailDisease)
        if(supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.tbDetailDisease?.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}