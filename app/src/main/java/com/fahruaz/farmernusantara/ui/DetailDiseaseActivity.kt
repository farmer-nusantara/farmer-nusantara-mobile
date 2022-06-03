package com.fahruaz.farmernusantara.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.fahruaz.farmernusantara.R
import com.fahruaz.farmernusantara.databinding.ActivityDetailDiseaseBinding
import com.fahruaz.farmernusantara.ml.CassavamodelV1D2
import com.fahruaz.farmernusantara.ml.CornmodelV1D1
import com.fahruaz.farmernusantara.ml.PaddymodelV1D3
import com.fahruaz.farmernusantara.ui.fragment.farmland.FarmlandFragment
import com.fahruaz.farmernusantara.util.reduceFileImage
import com.fahruaz.farmernusantara.viewmodels.DetailDiseaseViewModel
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.text.SimpleDateFormat
import java.util.*

class DetailDiseaseActivity : AppCompatActivity() {

    private var binding: ActivityDetailDiseaseBinding? = null
    private var customProgressDialog: Dialog? = null
    private lateinit var bitmap: Bitmap
    private lateinit var detailDiseaseViewModel: DetailDiseaseViewModel

    private var getFile: File? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude = 0.0
    private var longitude = 0.0
    private var diseasePlant = ""

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val farmlandId = intent.getStringExtra(FarmlandFragment.EXTRA_FARMLAND_ID)

        detailDiseaseViewModel = ViewModelProvider(this)[DetailDiseaseViewModel::class.java]

        MainActivity.imageStorageViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        MainActivity.imageStorageViewModel.toast.observe(this) {
            if(it.isNotEmpty()) {
                showToast(it)
            }
        }

        MainActivity.imageStorageViewModel.imageUrl.observe(this) {
            saveDiseasePlant(farmlandId!!, latitude, longitude, diseasePlant, it, MainActivity.userModel?.id!!)
        }

        detailDiseaseViewModel.toast.observe(this) {
            if(it.isNotEmpty())
                showToast(it)
            if(it == "Berhasil menyimpan penyakit") {
                finish()
            }
        }

        binding?.ivDisease?.setImageURI(Uri.parse(DetailFarmlandActivity.uriString))
        getFile = File(DetailFarmlandActivity.uriString)

        val uri = Uri.parse("file://${DetailFarmlandActivity.uriString}")

        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)

        getCurrentDate()
        imageProcess()

        binding?.saveDataBtn?.setOnClickListener {
            requestLocationPermission()
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if(resultCode == Activity.RESULT_OK){
//            val uri = Uri.parse(DetailFarmlandActivity.uriString)
//
//            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
//        }
//    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(){
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        binding?.tvDate?.text = currentDateAndTime
    }

    private fun imageProcess(){
        if (DetailFarmlandActivity.plant == "Jagung"){
            val list = getFileName("cornclasses_v1_d1.txt")
            val model = CornmodelV1D1.newInstance(this)

            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 150, 150, 3), DataType.FLOAT32)
            inputFeature0.loadBuffer(getImageData(150))

            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            val max = getMax(outputFeature0.floatArray, 3)
            val name = list[max]
            diseasePlant = name

            model.close()

            binding?.tvTitleDisease?.text = name

        }else if(DetailFarmlandActivity.plant == "Singkong"){
            val list = getFileName("cassavaclasses_v1_d2.txt")
            val model = CassavamodelV1D2.newInstance(this)

            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 150, 150, 3), DataType.FLOAT32)
            inputFeature0.loadBuffer(getImageData(150))

            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            val max = getMax(outputFeature0.floatArray, 3)
            val name = list[max]
            diseasePlant = name

            model.close()

            binding?.tvTitleDisease?.text = name

        }else if(DetailFarmlandActivity.plant == "Padi") {
            val list = getFileName("paddyclasses_v1_d1.txt")
            val model = PaddymodelV1D3.newInstance(this)

            val inputFeature0 =
                TensorBuffer.createFixedSize(intArrayOf(1, 150, 150, 3), DataType.FLOAT32)
            inputFeature0.loadBuffer(getImageData(150))

            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            val max = getMax(outputFeature0.floatArray, 3)
            val name = list[max]
            diseasePlant = name

            model.close()

            binding?.tvTitleDisease?.text = name
        }
    }

    private fun getFileName(name: String): List<String>{
        val inputString = application.assets.open(name).bufferedReader().use { it.readText() }
        return inputString.split("\n")
    }

    private fun getImageData(num: Int) : ByteBuffer {
        val resized : Bitmap = Bitmap.createScaledBitmap(bitmap, num, num, true)
        val imgData: ByteBuffer = ByteBuffer.allocateDirect(Float.SIZE_BYTES * num * num * 3)
        imgData.order(ByteOrder.nativeOrder())

        val intValues = IntArray(num * num)
        resized.getPixels(intValues, 0, resized.width, 0, 0, resized.width, resized.height)

        var pixel = 0
        for (i in 0 until num) {
            for (j in 0 until num) {
                val `val` = intValues[pixel++]
                imgData.putFloat((`val` shr 16 and 0xFF) / 255f)
                imgData.putFloat((`val` shr 8 and 0xFF) / 255f)
                imgData.putFloat((`val` and 0xFF) / 255f)
            }
        }
        return imgData
    }

    private fun getMax(arr: FloatArray, j: Int): Int{
        var ind = 0
        var min = 0.0F

        for (i in 0..j)
        {
            if(arr[i] > min){
                ind = i
                min = arr[i]
            }
        }
        return ind
    }

    private fun saveDiseasePlant(farmlandId: String, latitude: Double, longitude: Double,
                                 diseasePlant: String, imageUrl: String, picturedBy: String)
    {
        detailDiseaseViewModel.saveDiseasePlant(farmlandId, latitude, longitude, diseasePlant, imageUrl, picturedBy)
    }

    private fun uploadImage() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart = MultipartBody.Part.createFormData("file", file.name, requestImageFile)

            MainActivity.imageStorageViewModel.uploadImageToStorage(MainActivity.userModel?.id!!, imageMultipart)
        }
    }

    private fun requestLocationPermission() {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    runBlocking {
                        getMyLocation()
                        uploadImage()
                    }
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

    private fun getMyLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                Log.e("Location", it.toString())
                latitude = it.latitude
                longitude = it.longitude
            }
            else {
                val mLocationRequest: LocationRequest = LocationRequest.create()
                mLocationRequest.interval = 60000
                mLocationRequest.fastestInterval = 5000
                mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                val mLocationCallback: LocationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        for (location in locationResult.locations) {
                            if (location != null) {
                                latitude = it?.latitude!!
                                longitude = it.longitude
                            }
                        }
                    }
                }
                LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, mLocationCallback, null)
            }
        }
    }

    private fun showSettingsDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Butuh izin lokasi")
        builder.setMessage("Aktifkan lokasi untuk menyimpan penyakit.")
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

}