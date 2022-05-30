package com.fahruaz.farmernusantara.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.bumptech.glide.Glide
import com.fahruaz.farmernusantara.databinding.ActivityImageConfirmationBinding

class ImageConfirmationActivity : AppCompatActivity() {

    private var binding: ActivityImageConfirmationBinding? = null

    private var image : String? = null

    private lateinit var bitmap: Bitmap

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == DetailFarmlandActivity.CODE_CAMERA){
            if(resultCode == Activity.RESULT_OK){
                val uri = data?.getStringExtra(CameraActivity.CEK_URI)
                Glide.with(this)
                    .load(uri)
                    .into(binding?.ivDisease!!)

                image = uri
                val u = Uri.parse(uri)
                bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, u)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}