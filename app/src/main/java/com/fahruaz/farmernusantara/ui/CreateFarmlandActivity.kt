package com.fahruaz.farmernusantara.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.fahruaz.farmernusantara.R
import com.fahruaz.farmernusantara.databinding.ActivityCreateFarmlandBinding
import yuku.ambilwarna.AmbilWarnaDialog

class CreateFarmlandActivity : AppCompatActivity() {

    private var binding: ActivityCreateFarmlandBinding? = null

    private var mDefaultColor = 0

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

        binding?.previewSelectedColor?.setOnClickListener {
            openColorPickerDialogue()
        }
    }

    private fun openColorPickerDialogue() {
        val colorPickerDialogue = AmbilWarnaDialog(this, mDefaultColor,
            object : AmbilWarnaDialog.OnAmbilWarnaListener {
                override fun onCancel(dialog: AmbilWarnaDialog) {
                    // Do nothing
                }
                override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
                    mDefaultColor = color
                    binding?.previewSelectedColor?.setColorFilter(mDefaultColor)
                }
            })
        colorPickerDialogue.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}