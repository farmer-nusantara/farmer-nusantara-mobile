package com.fahruaz.farmernusantara.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.fahruaz.farmernusantara.R
import com.fahruaz.farmernusantara.databinding.ActivityDetailFarmlandBinding

class DetailFarmlandActivity : AppCompatActivity() {

    private var binding: ActivityDetailFarmlandBinding? = null

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

        // fab
        binding?.fabCollapse?.setOnClickListener {
            onAddButtonCLicked()
        }
        binding?.fabMap?.setOnClickListener {
            Toast.makeText(this, "Map", Toast.LENGTH_SHORT).show()
        }
        binding?.fabScan?.setOnClickListener {
            Toast.makeText(this, "Scan", Toast.LENGTH_SHORT).show()
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

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}