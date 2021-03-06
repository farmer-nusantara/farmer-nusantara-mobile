package com.fahruaz.farmernusantara.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.graphics.Bitmap
import android.location.LocationManager
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
import com.fahruaz.farmernusantara.ml.CornmodelV1D3
import com.fahruaz.farmernusantara.ml.PaddymodelV1D4
import com.fahruaz.farmernusantara.ml.SoybeanmodelV1d2
import com.fahruaz.farmernusantara.ui.fragment.farmland.FarmlandFragment
import com.fahruaz.farmernusantara.util.reduceFileImage
import com.fahruaz.farmernusantara.viewmodels.DetailDiseaseViewModel
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
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
    private lateinit var uri: Uri

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var googleApiClient: GoogleApiClient? = null
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
            backDialog()
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
            if(it.isNotEmpty()) {
                saveDiseasePlant(farmlandId!!, latitude, longitude, diseasePlant, it, MainActivity.userModel?.id!!)
            }
        }

        detailDiseaseViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailDiseaseViewModel.toast.observe(this) {
            if(it.isNotEmpty()) {
                showToast(it)
            }
            if(it == "Berhasil menyimpan penyakit") {
                finish()
            }
        }

        binding?.ivDisease?.setImageURI(Uri.parse(DetailFarmlandActivity.uriString))
        getFile = File(DetailFarmlandActivity.uriString)

        uri = Uri.parse("file://${DetailFarmlandActivity.uriString}")

        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)

        getCurrentDate()
        imageProcess()

        binding?.saveDataBtn?.setOnClickListener {
            requestLocationPermission()
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(){
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        binding?.tvDate?.text = currentDateAndTime
    }

    private fun imageProcess(){
        if (DetailFarmlandActivity.plant == "Jagung"){
            val list = getFileName("cornclasses_v1_d3.txt")
            val model = CornmodelV1D3.newInstance(this)

            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 150, 150, 3), DataType.FLOAT32)
            inputFeature0.loadBuffer(getImageData(150))

            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            val max = getMax(outputFeature0.floatArray, 3)
            val name = list[max]
            diseasePlant = name

            model.close()

            binding?.tvTitleDisease?.text = name
        }
        else if(DetailFarmlandActivity.plant == "Kedelai"){
            val list = getFileName("soybeanclasses_v1d2.txt")
            val model = SoybeanmodelV1d2.newInstance(this)

            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 150, 150, 3), DataType.FLOAT32)
            inputFeature0.loadBuffer(getImageData(150))

            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            val max = getMax(outputFeature0.floatArray, 1)
            val name = list[max]
            diseasePlant = name

            model.close()

            binding?.tvTitleDisease?.text = name
        }
        else if(DetailFarmlandActivity.plant == "Padi") {
            val list = getFileName("paddyclasses_v1_d1.txt")
            val model = PaddymodelV1D4.newInstance(this)

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

        when(binding?.tvTitleDisease?.text?.trim().toString()) {
            "Common_Rust" -> {
                binding?.tvTitleDisease?.text = resources.getString(R.string.Common_Rust)
                binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Common_Rust)
                binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Common_Rust)
            }
            "Northern_Leaf_Blight" -> {
                binding?.tvTitleDisease?.text = resources.getString(R.string.Northern_Leaf_Blight)
                binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Northern_Leaf_Blight)
                binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Northern_Leaf_Blight)
            }
            "Cercospora_Leaf_Spot_Gray_Leaf_Spot" -> {
                binding?.tvTitleDisease?.text = resources.getString(R.string.Cercospora_Leaf_Spot_Gray_Leaf_Spot)
                binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Cercospora_Leaf_Spot_Gray_Leaf_Spot)
                binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Cercospora_Leaf_Spot_Gray_Leaf_Spot)
            }
            "Bacterial Leaf Blight" -> {
                binding?.tvTitleDisease?.text = resources.getString(R.string.Bacterial_Leaf_Blight)
                binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Bacterial_Leaf_Blight)
                binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Bacterial_Leaf_Blight)
            }
            "Bacterial Leaf Streak" -> {
                binding?.tvTitleDisease?.text = resources.getString(R.string.Bacterial_Leaf_Streak)
                binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Bacterial_Leaf_Streak)
                binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Bacterial_Leaf_Streak)
            }
            "Bacterial Panicle Blight" -> {
                binding?.tvTitleDisease?.text = resources.getString(R.string.Bacterial_Panicle_Blight)
                binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Bacterial_Panicle_Blight)
                binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Bacterial_Panicle_Blight)
            }
            "Blast" -> {
                binding?.tvTitleDisease?.text = resources.getString(R.string.Blast)
                binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Blast)
                binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Blast)
            }
            "Brown Spot" -> {
                binding?.tvTitleDisease?.text = resources.getString(R.string.Brown_Spot)
                binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Brown_Spot)
                binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Brown_Spot)
            }
            "Dead Heart" -> {
                binding?.tvTitleDisease?.text = resources.getString(R.string.Dead_Heart)
                binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Dead_Heart)
                binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Dead_Heart)
            }
            "Down Mildew" -> {
                binding?.tvTitleDisease?.text = resources.getString(R.string.Down_Mildew)
                binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Down_Mildew)
                binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Down_Mildew)
            }
            "Hispa" -> {
                binding?.tvTitleDisease?.text = resources.getString(R.string.Hispa)
                binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Hispa)
                binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Hispa)
            }
            "Tungro" -> {
                binding?.tvTitleDisease?.text = resources.getString(R.string.Tungro)
                binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_Tungro)
                binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_Tungro)
            }
            "angular_leaf_spot" -> {
                binding?.tvTitleDisease?.text = resources.getString(R.string.angular_leaf_spot)
                binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_angular_leaf_spot)
                binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_angular_leaf_spot)
            }
            "bean_rust" -> {
                binding?.tvTitleDisease?.text = resources.getString(R.string.bean_rust)
                binding?.tvDescriptionDisease?.text = resources.getString(R.string.Desc_bean_rust)
                binding?.tvRecommendationCare?.text = resources.getString(R.string.Treatment_bean_rust)
            }
            else -> {
                binding?.tvTitleDisease?.text = resources.getString(R.string.healthy)
                binding?.tvDescriptionDisease?.text = resources.getString(R.string.healthyPlant)
                binding?.tvRecommendationCare?.text = resources.getString(R.string.empty)
            }
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
                    if(isGpsEnabled()) {
                        runBlocking {
                            // enableLoc()
                            getMyLocation()
                            uploadImage()
                            DetailFarmlandActivity.isSaveBtnClicked = true
                        }
                    }
                    else {
                        showToEnableGPS()
                    }

                }
                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    // check for permanent denial of permission
                    if (response!!.isPermanentlyDenied) {
                        showSettingsDialogLoc()
                    }
                }
                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private fun isGpsEnabled(): Boolean {
        val mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
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

    private fun backDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Data penyakit tidak disimpan")
        builder.setMessage("Apakah Anda yakin untuk kembali? Jika Anda kembali, data penyakin tidak akan disimpan.")
        builder.setPositiveButton("Kembali") { dialog, _ ->
            dialog.cancel()
            onBackPressed()
        }
        builder.setNegativeButton("Tidak") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private fun showSettingsDialogLoc() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Butuh izin lokasi")
        builder.setMessage("Aktifkan lokasi untuk menyimpan penyakit.")
        builder.setPositiveButton("Pengaturan") { dialog, _ ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton("Keluar") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private fun showToEnableGPS() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("GPS tidak aktif")
        builder.setMessage("Aktifkan GPS untuk menyimpan penyakit.")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.cancel()
            enableLoc()
        }
        builder.show()
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    private fun enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                    override fun onConnected(bundle: Bundle?) {}
                    override fun onConnectionSuspended(i: Int) {
                        googleApiClient?.connect()
                    }
                })
                .addOnConnectionFailedListener { connectionResult ->
                    Log.d(
                        "Location error",
                        "Location error " + connectionResult.errorCode
                    )
                }.build()
            googleApiClient?.connect()
            val locationRequest: LocationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 30 * 1000
            locationRequest.fastestInterval = 5 * 1000
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
            builder.setAlwaysShow(true)
            val result: PendingResult<LocationSettingsResult> =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient!!, builder.build())
            result.setResultCallback { result ->
                val status: Status = result.status
                when (status.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
//                        status.startResolutionForResult(this, MapFragment.REQUEST_LOCATION)
                        // do nothing
                    } catch (e: IntentSender.SendIntentException) {
                        e.printStackTrace()
                    }
                }
            }
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
        customProgressDialog?.setCancelable(false)
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