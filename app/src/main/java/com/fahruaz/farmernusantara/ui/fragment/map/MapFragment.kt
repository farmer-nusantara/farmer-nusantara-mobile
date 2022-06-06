package com.fahruaz.farmernusantara.ui.fragment.map

import android.Manifest
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fahruaz.farmernusantara.R
import com.fahruaz.farmernusantara.databinding.FragmentMapBinding
import com.fahruaz.farmernusantara.response.farmland.ShowFarmlandDetailResponse
import com.fahruaz.farmernusantara.response.plantdisease.GetAllSickPlantsResponseItem
import com.fahruaz.farmernusantara.ui.DetailFarmlandActivity
import com.fahruaz.farmernusantara.ui.MainActivity
import com.fahruaz.farmernusantara.ui.fragment.farmland.FarmlandFragment
import com.fahruaz.farmernusantara.viewmodels.MapActivityViewModel
import com.fahruaz.farmernusantara.viewmodels.OwnerMapViewModel
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MapFragment : Fragment() {

    private var binding: FragmentMapBinding? = null

    private lateinit var mMap : GoogleMap
    private var googleApiClient: GoogleApiClient? = null
    private var customProgressDialog: Dialog? = null
    private lateinit var mapsViewModel: OwnerMapViewModel
    private var listDiseases = ArrayList<GetAllSickPlantsResponseItem>()

    private val callback = OnMapReadyCallback { googleMap ->
        mapsViewModel = ViewModelProvider(this)[OwnerMapViewModel::class.java]

        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isIndoorLevelPickerEnabled = true
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = true

        mapsViewModel.getAllDiseaseOwner("Token ${MainActivity.userModel?.token}", MainActivity.userModel?.id!!)

        Log.e("ID", MainActivity.userModel?.id!!)

        mapsViewModel.listDiseases.observe(this) { disease ->
            setDiseasesData(disease)
        }

        getMyLocation(googleMap)
        setMapStyle()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation(mMap)
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        mapsViewModel = ViewModelProvider(this)[OwnerMapViewModel::class.java]

        mapsViewModel.isLoading.observe(this.requireActivity()) {
            showLoading(it)
        }

        mapsViewModel.toast.observe(this.requireActivity()) {
            showToast(it)
        }

        enableLoc()

        activity?.findViewById<FloatingActionButton>(R.id.fabFarmland)?.setOnClickListener {
            findNavController().navigate(R.id.action_mapFragment_to_farmlandFragment)
        }

        // get reference to zoom in/out icon
        val locationButton = (mapFragment!!.requireView().findViewById<View>("1".toInt()).parent as View).findViewById<View>("1".toInt())
        // and next place it, for example, on bottom right (as Google Maps app)
        val rlp = locationButton.layoutParams as RelativeLayout.LayoutParams
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        rlp.setMargins(0, 0, 16, 186)

        FarmlandFragment.requestApi = false
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this.requireContext(), R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    private fun getMyLocation(googleMaps: GoogleMap) {
        mMap = googleMaps

        if (ContextCompat.checkSelfPermission(this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(requireContext())
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
                        status.startResolutionForResult(requireActivity(), REQUEST_LOCATION)
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
                farmlandId = disease.farmlandId,
                createdAt = disease.createdAt,
                latitude = disease.latitude,
                longitude = disease.longitude,
                imageUrl = disease.imageUrl,
                diseasePlant = disease.diseasePlant,
                picturedBy = disease.picturedBy,
                V = disease.V
            )
            this.listDiseases.add(newDisease)
            val latLng = LatLng(disease.latitude!!, disease.longitude!!)

            var diseasePlant = ""
            when(disease.diseasePlant?.trim()) {
                "Common_Rust" -> {
                    diseasePlant = resources.getString(R.string.Common_Rust)
                }
                "Northern_Leaf_Blight" -> {
                    diseasePlant = resources.getString(R.string.Northern_Leaf_Blight)
                }
                "Cercospora_Leaf_Spot_Gray_Leaf_Spot" -> {
                    diseasePlant = resources.getString(R.string.Cercospora_Leaf_Spot_Gray_Leaf_Spot)
                }
                "Bacterial Leaf Blight" -> {
                    diseasePlant = resources.getString(R.string.Bacterial_Leaf_Blight)
                }
                "Bacterial Leaf Streak" -> {
                    diseasePlant = resources.getString(R.string.Bacterial_Leaf_Streak)
                }
                "Bacterial Panicle Blight" -> {
                    diseasePlant = resources.getString(R.string.Bacterial_Panicle_Blight)
                }
                "Blast" -> {
                    diseasePlant = resources.getString(R.string.Blast)
                }
                "Brown Spot" -> {
                    diseasePlant = resources.getString(R.string.Brown_Spot)
                }
                "Dead Heart" -> {
                    diseasePlant = resources.getString(R.string.Dead_Heart)
                }
                "Down Mildew" -> {
                    diseasePlant = resources.getString(R.string.Down_Mildew)
                }
                "Hispa" -> {
                    diseasePlant = resources.getString(R.string.Hispa)
                }
                "Tungro" -> {
                    diseasePlant = resources.getString(R.string.Tungro)
                }
                "angular_leaf_spot" -> {
                    diseasePlant = resources.getString(R.string.angular_leaf_spot)
                }
                "bean_rust" -> {
                    diseasePlant = resources.getString(R.string.bean_rust)
                }
            }

            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(diseasePlant)
                    .snippet("Jenis tanaman: ${disease.farmlandId?.plantType}")
                    .icon(vectorToBitmap(R.drawable.virus, Color.parseColor(disease.farmlandId?.markColor)))
            )
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(disease.latitude, disease.longitude), 15f))
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(this.requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading)
            showProgressDialog()
        else
            cancelProgressDialog()
    }

    private fun showProgressDialog() {
        customProgressDialog = Dialog(this.requireContext())
        customProgressDialog?.setContentView(R.layout.dialog_custom_progressbar)
        customProgressDialog?.show()
    }

    private fun cancelProgressDialog() {
        if (customProgressDialog != null) {
            customProgressDialog?.dismiss()
            customProgressDialog = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val REQUEST_LOCATION = 199
    }

}