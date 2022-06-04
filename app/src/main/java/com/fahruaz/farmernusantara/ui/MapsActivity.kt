package com.fahruaz.farmernusantara.ui

import android.Manifest
import android.app.Dialog
import android.content.ContentValues
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.ViewModelProvider
import com.fahruaz.farmernusantara.R
import com.fahruaz.farmernusantara.databinding.ActivityMapsBinding
import com.fahruaz.farmernusantara.response.plantdisease.GetAllSickPlantsResponseItem
import com.fahruaz.farmernusantara.ui.fragment.farmland.FarmlandFragment
import com.fahruaz.farmernusantara.viewmodels.MapActivityViewModel
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var googleApiClient: GoogleApiClient? = null
//    private var customProgressDialog: Dialog? = null
    private lateinit var mapsViewModel: MapActivityViewModel
    private var listDiseases = ArrayList<GetAllSickPlantsResponseItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        enableLoc()

    }

    override fun onMapReady(googleMap: GoogleMap) {

        mapsViewModel = ViewModelProvider(this)[MapActivityViewModel::class.java]

        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        val id = intent.getStringExtra(FarmlandFragment.EXTRA_FARMLAND_ID)

        mapsViewModel.getAllDiseases("Token ${MainActivity.userModel?.token}", id!!)

        Log.e("id", id)
        Log.e("id",MainActivity.userModel?.token.toString())

        mapsViewModel.listDiseases.observe(this) { disease ->
            setDiseasesData(disease)
        }

        getMyLocation()
        setMapStyle()
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(applicationContext, R.raw.map_style))
            if (!success) {
                Log.e(ContentValues.TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(ContentValues.TAG, "Can't find style. Error: ", exception)
        }
    }

    private fun enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(applicationContext)
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
                        status.startResolutionForResult(this, REQUEST_LOCATION)
                    } catch (e: IntentSender.SendIntentException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)
        if (vectorDrawable == null) {
            Log.e("BitmapHelper", "Resource not found")
            return BitmapDescriptorFactory.defaultMarker()
        }
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, color)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun setDiseasesData(diseases: List<GetAllSickPlantsResponseItem>) {

        for (disease in diseases){
            val newDisease = GetAllSickPlantsResponseItem(
                id = disease.id,
                farmlandId = disease.farmlandId,
                latitude = disease.latitude,
                longitude = disease.longitude,
                diseasePlant = disease.diseasePlant,
                imageUrl = disease.imageUrl,
                createdAt = disease.createdAt,
                picturedBy = disease.picturedBy,
                V = disease.V
            )
            this.listDiseases.add(newDisease)
            val latLng = LatLng(disease.latitude!!, disease.longitude!!)

            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(disease.diseasePlant)
                    .snippet("Lat: ${latLng.latitude} Long: ${latLng.longitude}")
                    .icon(vectorToBitmap(R.drawable.virus, Color.parseColor("#3DDC84")))
            )
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(disease.latitude, disease.longitude), 5f))
        }

        Log.e("Lat", listDiseases.toString())

    }


    companion object {
        const val REQUEST_LOCATION = 199
    }
}