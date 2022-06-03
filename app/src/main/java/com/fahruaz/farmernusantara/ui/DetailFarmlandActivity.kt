package com.fahruaz.farmernusantara.ui

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.fahruaz.farmernusantara.R
import com.fahruaz.farmernusantara.databinding.ActivityDetailFarmlandBinding
import com.fahruaz.farmernusantara.response.farmland.ShowFarmlandDetailResponse
import com.fahruaz.farmernusantara.ui.fragment.farmland.FarmlandFragment
import com.fahruaz.farmernusantara.util.RealPathUtil
import com.fahruaz.farmernusantara.viewmodels.DetailFarmlandViewModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception

class DetailFarmlandActivity : AppCompatActivity() {

    private var binding: ActivityDetailFarmlandBinding? = null
    private var customProgressDialog: Dialog? = null
    private lateinit var farmlandDetail: ShowFarmlandDetailResponse

    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    // fab expandable
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim) }
    private var isClicked = false

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result = BitmapFactory.decodeFile(myFile.path)
            lifecycleScope.launch {
                saveBitmapFile(result)
                val intent = Intent(this@DetailFarmlandActivity, DetailDiseaseActivity::class.java)
                intent.putExtra(FarmlandFragment.EXTRA_FARMLAND_ID, farmlandDetail.id)
                startActivity(intent)
            }
        }
    }

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
                farmlandDetail = it
                plant = it.plantType!!
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
//            startActivityForResult(Intent(this, CameraActivity::class.java), CODE_CAMERA)
            requestStorageAndCameraPermission()
        }
        binding?.fabEdit?.setOnClickListener {
            val intent = Intent(this, EditFarmlandActivity::class.java)
            intent.putExtra(FarmlandFragment.EXTRA_FARMLAND, farmlandDetail)
            startActivity(intent)
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
            binding?.fabEdit?.visibility = View.VISIBLE
            binding?.tvLabelFabMap?.visibility = View.VISIBLE
            binding?.tvLabelFabScan?.visibility = View.VISIBLE
            binding?.tvLabelFabEdit?.visibility = View.VISIBLE
        }
        else {
            binding?.fabMap?.visibility = View.INVISIBLE
            binding?.fabScan?.visibility = View.INVISIBLE
            binding?.fabEdit?.visibility = View.INVISIBLE
            binding?.tvLabelFabMap?.visibility = View.INVISIBLE
            binding?.tvLabelFabScan?.visibility = View.INVISIBLE
            binding?.tvLabelFabEdit?.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if(!clicked) {
            binding?.fabMap?.startAnimation(fromBottom)
            binding?.fabScan?.startAnimation(fromBottom)
            binding?.fabEdit?.startAnimation(fromBottom)
            binding?.tvLabelFabMap?.startAnimation(fromBottom)
            binding?.tvLabelFabScan?.startAnimation(fromBottom)
            binding?.tvLabelFabEdit?.startAnimation(fromBottom)
            binding?.fabCollapse?.startAnimation(rotateOpen)
        }
        else {
            binding?.fabMap?.startAnimation(toBottom)
            binding?.fabScan?.startAnimation(toBottom)
            binding?.fabEdit?.startAnimation(toBottom)
            binding?.tvLabelFabMap?.startAnimation(toBottom)
            binding?.tvLabelFabScan?.startAnimation(toBottom)
            binding?.tvLabelFabEdit?.startAnimation(toBottom)
            binding?.fabCollapse?.startAnimation(rotateClose)
        }
    }

    private fun setClickable(clicked: Boolean) {
        if(!clicked) {
            binding?.fabMap?.isClickable = true
            binding?.fabScan?.isClickable = true
            binding?.fabEdit?.isClickable = true
        }
        else {
            binding?.fabMap?.isClickable = false
            binding?.fabScan?.isClickable = false
            binding?.fabEdit?.isClickable = false
        }
    }

    private fun requestStorageAndCameraPermission() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    // check if all permissions are granted
                    if (report!!.areAllPermissionsGranted()) {
                        startTakePhoto()
                    }

                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) {
                        // show alert dialog navigating to Settings
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).withErrorListener {
                Toast.makeText(applicationContext, "Terjadi kesalahan!", Toast.LENGTH_SHORT).show()
            }
            .onSameThread()
            .check()
    }

    private fun requestCameraPermission() {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    startTakePhoto()
                }
                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    // check for permanent denial of permission
                    if (response!!.isPermanentlyDenied) {
                        showSettingsDialog()
                    }
                }
                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private fun showSettingsDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Butuh izin kamera dan penyimpanan")
        builder.setMessage("Untuk mengambil foto, butuh izin kamera dan penyimpanan.")
        builder.setPositiveButton("Pengaturan") { dialog, _ ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        com.fahruaz.farmernusantara.util.createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(this, "com.fahruaz.farmernusantara", it)
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private suspend fun saveBitmapFile(bitmap: Bitmap?): String {
        var result = ""

        withContext(Dispatchers.IO) {
            if(bitmap != null) {
                try {
                    val filename = "${System.currentTimeMillis()/1000}.png"
                    val mimeType =  "image/png"
                    val directory = Environment.DIRECTORY_PICTURES
                    val mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

                    val imageOutStream: OutputStream

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val values = ContentValues().apply {
                            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
                            put(MediaStore.Images.Media.RELATIVE_PATH, directory)
                        }

                        contentResolver.run {
                            val uri = contentResolver.insert(mediaContentUri, values)!!
                            imageOutStream = openOutputStream(uri)!!
                            result = uri.toString()
                            uriString =
                                RealPathUtil.getRealPath(this@DetailFarmlandActivity, Uri.parse(result)).toString()
                        }
                    }
                    else {
                        val imagePath = Environment.getExternalStoragePublicDirectory(directory).absolutePath
                        val image = File(imagePath, filename)
                        imageOutStream = FileOutputStream(image)
                        uriString = image.absolutePath
                    }

                    imageOutStream.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }

                    runOnUiThread {
                        cancelProgressDialog()
                        if(result.isNotEmpty())
                            Toast.makeText(this@DetailFarmlandActivity, "File saved succesfully: $uriString", Toast.LENGTH_LONG).show()
                        else
                            Toast.makeText(this@DetailFarmlandActivity, "Something went wrong while saving", Toast.LENGTH_LONG).show()
                    }

                }
                catch (e: Exception) {
                    result = ""
                    e.printStackTrace()
                }
            }
        }

        return result
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
        var uriString = ""
        var plant = ""
    }

}