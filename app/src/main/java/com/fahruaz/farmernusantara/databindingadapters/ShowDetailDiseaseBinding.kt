package com.fahruaz.farmernusantara.databindingadapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.fahruaz.farmernusantara.R
import java.text.SimpleDateFormat
import java.util.*

class ShowDetailDiseaseBinding {

    companion object {

        @BindingAdapter("loadImageFromUrlDiseaseDetail")
        @JvmStatic
        fun loadImageFromUrlDiseaseDetail(imageView: ImageView, imageUrl: String) {
            imageView.load(imageUrl) {
                crossfade(600)
                error(R.drawable.image_default)
            }
        }

        @BindingAdapter("setDiseaseNameDiseaseDetail")
        @JvmStatic
        fun setDiseaseNameDiseaseDetail(textView: TextView, name: String) {
            textView.text = name
        }

        @BindingAdapter("setDateDiseaseDetail")
        @JvmStatic
        fun setDateDiseaseDetail(textView: TextView, epoch: String) {
            val date = Date(epoch.toLong()*1000)
            val sdf = SimpleDateFormat("EEE, MMM d, HH:mm:ss")
            val formatted = sdf.format(date)
            textView.text = formatted
        }

    }

}