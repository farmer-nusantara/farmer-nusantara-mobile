package com.fahruaz.farmernusantara.ui

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.fahruaz.farmernusantara.R
import com.fahruaz.farmernusantara.databinding.ActivityCreateFarmlandBinding
import com.fahruaz.farmernusantara.ui.fragment.farmland.FarmlandFragment
import com.fahruaz.farmernusantara.util.reduceFileImage
import com.fahruaz.farmernusantara.util.uriToFile
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import yuku.ambilwarna.AmbilWarnaDialog
import java.io.File

class CreateFarmlandActivity : AppCompatActivity() {

    private var binding: ActivityCreateFarmlandBinding? = null
    private var customProgressDialog: Dialog? = null
    //    private lateinit var farmlandViewModel: FarmlandViewModel
    private var mDefaultColor = Color.parseColor("#E35B30")
    private var hexColor = "#E35B30"
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null
    private lateinit var farmlandName: String
    private lateinit var farmlandLocation: String
    private lateinit var farmlandPlantType: String

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result = BitmapFactory.decodeFile(myFile.path)
            binding?.ivFarmland?.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile
            binding?.ivFarmland?.setImageURI(selectedImg)
        }
    }

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

//        farmlandViewModel = ViewModelProvider(this)[FarmlandViewModel::class.java]

        FarmlandFragment.farmlandViewModel?.isLoading?.observe(this) {
            showLoading(it)
        }

        MainActivity.imageStorageViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        MainActivity.imageStorageViewModel.toast.observe(this) {
            if(it.isNotEmpty()) {
                showToast(it)
            }
        }

        MainActivity.imageStorageViewModel.imageUrl.observe(this) {
            createFarmland(farmlandName, farmlandLocation, farmlandPlantType, it)
        }

        FarmlandFragment.farmlandViewModel?.toastCreateFarmland?.observe(this) {
            if(it.isNotEmpty())
                showToast(it)
            if(it == "Berhasil membuat farmland") {
                finish()
                FarmlandFragment.farmlandViewModel?.toastCreateFarmland?.value = ""
            }
        }

        val plantTypes = resources.getStringArray(R.array.plantType)
        val arrayAdapter = ArrayAdapter(this, R.layout.plant_type_dropdown_item, plantTypes)
        binding?.plantTypeAutoComplete?.setAdapter(arrayAdapter)

        binding?.previewSelectedColor?.setOnClickListener {
            openColorPickerDialogue()
        }

        binding?.btCamera?.setOnClickListener { requestCameraPermission() }
        binding?.btGallery?.setOnClickListener { startGallery() }
        binding?.btCreateFarmland?.setOnClickListener {
            binding?.farmlandNameEditTextLayout?.error = ""
            binding?.farmlandLocationEditTextLayout?.error = ""
            imageUrl = ""

            farmlandName = binding?.farmlandNameEditText?.text.toString()
            farmlandLocation = binding?.farmlandLocationEditText?.text.toString()
            farmlandPlantType = binding?.plantTypeAutoComplete?.text.toString()

            when {
                farmlandName.isEmpty() -> {
                    binding?.farmlandNameEditTextLayout?.error = resources.getString(R.string.empty_farmland)
                }
                farmlandLocation.isEmpty() -> {
                    binding?.farmlandLocationEditTextLayout?.error = resources.getString(R.string.empty_location)
                }
                else -> {
                    FarmlandFragment.requestApi = true
                    uploadImage()
                }
            }
        }
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
        builder.setTitle("Butuh izin kamera")
        builder.setMessage("Untuk mengambil foto, butuh izin kamera.")
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
            val photoURI: Uri = FileProvider.getUriForFile(this@CreateFarmlandActivity, "com.fahruaz.farmernusantara", it)
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Pilih gambar")
        launcherIntentGallery.launch(chooser)
    }

    private fun uploadImage() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart = MultipartBody.Part.createFormData("file", file.name, requestImageFile)

            MainActivity.imageStorageViewModel.uploadImageToStorage(MainActivity.userModel?.id!!, imageMultipart)
        }
        else {
            createFarmland(farmlandName, farmlandLocation, farmlandPlantType, "")
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
                    hexColor = String.format("#%06X", 0xFFFFFF and mDefaultColor)
                    binding?.previewSelectedColor?.setColorFilter(mDefaultColor)
                }
            })
        colorPickerDialogue.show()
    }

    private fun createFarmland(name: String, location: String, plantTYpe: String, imageUrl2: String) {
        FarmlandFragment.farmlandViewModel?.createFarmland(name, MainActivity.userModel?.id!!, hexColor, plantTYpe, location, imageUrl2)
        FarmlandFragment.requestApi = true
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

    companion object {
        var imageUrl = ""
    }

}