package com.fahruaz.farmernusantara.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.fahruaz.farmernusantara.R
import com.fahruaz.farmernusantara.databinding.ActivityCreateFarmlandBinding

class CreateFarmlandActivity : AppCompatActivity() {

    private var binding: ActivityCreateFarmlandBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateFarmlandBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // toolbar
        setSupportActionBar(binding?.tbCreateFarmland)
        if(supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.tbCreateFarmland?.setNavigationOnClickListener {
            onBackPressed()
        }

        val plantTypes = resources.getStringArray(R.array.plantType)
        val arrayAdapter = ArrayAdapter(this, R.layout.plant_type_dropdown_item, plantTypes)
        binding?.plantTypeAutoComplete?.setAdapter(arrayAdapter)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}