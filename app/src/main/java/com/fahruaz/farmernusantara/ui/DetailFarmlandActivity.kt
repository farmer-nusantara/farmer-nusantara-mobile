package com.fahruaz.farmernusantara.ui

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.fahruaz.farmernusantara.R
import com.fahruaz.farmernusantara.databinding.ActivityDetailFarmlandBinding
import com.fahruaz.farmernusantara.ui.fragment.farmland.FarmlandFragment
import com.fahruaz.farmernusantara.viewmodels.DetailFarmlandViewModel
import kotlinx.android.synthetic.main.activity_login.*

class DetailFarmlandActivity : AppCompatActivity() {

    private var binding: ActivityDetailFarmlandBinding? = null
    private var customProgressDialog: Dialog? = null

    // fab expandable
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim) }
    private var isClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFarmlandBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // toolbar
        setSupportActionBar(binding?.tbDetailFarmland)
        if(supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.tbDetailFarmland?.setNavigationOnClickListener {
            onBackPressed()
        }

        val farmlandId = intent.getStringExtra(FarmlandFragment.EXTRA_FARMLAND_ID)

        val detailFarmlandViewModel = ViewModelProvider(this)[DetailFarmlandViewModel::class.java]

        if(farmlandId != null) {
            detailFarmlandViewModel.getAllFarmlandByOwner(farmlandId, MainActivity.userModel?.token!!)
            detailFarmlandViewModel.farmland.observe(this) {
                binding?.result = it
                val hexColorToInt = Color.parseColor(it.markColor)
                binding?.ivFarmlandColor?.setColorFilter(hexColorToInt)
                Glide.with(this)
                    .load(it.imageUrl)
                    .placeholder(R.drawable.image_default)
                    .into(binding?.ivFarmland!!)
            }
        }

        detailFarmlandViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        // fab
        binding?.fabCollapse?.setOnClickListener {
            onAddButtonCLicked()
        }
        binding?.fabMap?.setOnClickListener {
            val intent = Intent(applicationContext, MapsActivity::class.java)
            startActivity(intent)
        }
        binding?.fabScan?.setOnClickListener {
            startActivityForResult(Intent(this, CameraActivity::class.java), CODE_CAMERA)
//            val intent = Intent(applicationContext, CameraActivity::class.java)
//            startActivity(intent)
        }

    }

    private fun onAddButtonCLicked() {
        setVisibility(isClicked)
        setAnimation(isClicked)
        setClickable(isClicked)
        isClicked = !isClicked
    }

    private fun setVisibility(clicked: Boolean) {
        if(!clicked) {
            binding?.fabMap?.visibility = View.VISIBLE
            binding?.fabScan?.visibility = View.VISIBLE
            binding?.tvLabelFabMap?.visibility = View.VISIBLE
            binding?.tvLabelFabScan?.visibility = View.VISIBLE
        }
        else {
            binding?.fabMap?.visibility = View.INVISIBLE
            binding?.fabScan?.visibility = View.INVISIBLE
            binding?.tvLabelFabMap?.visibility = View.INVISIBLE
            binding?.tvLabelFabScan?.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if(!clicked) {
            binding?.fabMap?.startAnimation(fromBottom)
            binding?.fabScan?.startAnimation(fromBottom)
            binding?.tvLabelFabMap?.startAnimation(fromBottom)
            binding?.tvLabelFabScan?.startAnimation(fromBottom)
            binding?.fabCollapse?.startAnimation(rotateOpen)
        }
        else {
            binding?.fabMap?.startAnimation(toBottom)
            binding?.fabScan?.startAnimation(toBottom)
            binding?.tvLabelFabMap?.startAnimation(toBottom)
            binding?.tvLabelFabScan?.startAnimation(toBottom)
            binding?.fabCollapse?.startAnimation(rotateClose)
        }
    }

    private fun setClickable(clicked: Boolean) {
        if(!clicked) {
            binding?.fabMap?.isClickable = true
            binding?.fabScan?.isClickable = true
        }
        else {
            binding?.fabMap?.isClickable = false
            binding?.fabScan?.isClickable = false
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

    companion object{
        const val CODE_CAMERA = 1
    }

}