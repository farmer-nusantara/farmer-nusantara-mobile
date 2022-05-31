package com.fahruaz.farmernusantara.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.fahruaz.farmernusantara.databinding.ActivityDetailDiseaseBinding
import com.fahruaz.farmernusantara.ml.CassavamodelV1D2
import com.fahruaz.farmernusantara.ml.CornmodelV1D1
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.text.SimpleDateFormat
import java.util.*

class DetailDiseaseActivity : AppCompatActivity() {

    private var binding: ActivityDetailDiseaseBinding? = null
    private lateinit var bitmap: Bitmap

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

        binding?.ivDisease?.setImageURI(Uri.parse(DetailFarmlandActivity.uriString))

        val uri = Uri.parse("file://${DetailFarmlandActivity.uriString}")

        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)

        getCurrentDate()
        imageProcess()
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
        val list = getFileName("cornclasses_v1_d1.txt")
        val model = CornmodelV1D1.newInstance(this)

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 150, 150, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(getImageData(150))

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val max = getMax(outputFeature0.floatArray, 3)
        val name = list[max]

        model.close()

        binding?.tvTitleDisease?.text = name
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

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}