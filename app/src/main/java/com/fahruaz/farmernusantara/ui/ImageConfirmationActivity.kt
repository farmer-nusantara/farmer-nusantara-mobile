package com.fahruaz.farmernusantara.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fahruaz.farmernusantara.databinding.ActivityImageConfirmationBinding

class ImageConfirmationActivity : AppCompatActivity() {

    private var binding: ActivityImageConfirmationBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageConfirmationBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // toolbar
        setSupportActionBar(binding?.tbImageConfirmation)
        if(supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.tbImageConfirmation?.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}