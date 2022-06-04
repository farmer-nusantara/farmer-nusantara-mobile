package com.fahruaz.farmernusantara.databindingadapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.fahruaz.farmernusantara.R
import java.text.SimpleDateFormat
import java.util.*

class DiseaseHistoryBinding {

    companion object {

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {
            imageView.load(imageUrl) {
                crossfade(600)
                error(R.drawable.image_default)
            }
        }

        @BindingAdapter("setDiseaseName")
        @JvmStatic
        fun setDiseaseName(textView: TextView, name: String) {
            textView.text = name
        }

        @BindingAdapter("setDate")
        @JvmStatic
        fun setDate(textView: TextView, epoch: String) {
            val date = Date(epoch.toLong()*1000)
            val sdf = SimpleDateFormat("EEE, MMM d, HH:mm:ss")
            val formatted = sdf.format(date)
            textView.text = formatted
        }

    }

}