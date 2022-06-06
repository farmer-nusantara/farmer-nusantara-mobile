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
            when(name.trim()) {
                "Common_Rust" -> {
                    textView.text = "Karat Daun / Karat Biasa"
                }
                "Northern_Leaf_Blight" -> {
                    textView.text = "Hawar Daun"
                }
                "Cercospora_Leaf_Spot_Gray_Leaf_Spot" -> {
                    textView.text = "Bercak Daun Cercospora"
                }
                "Bacterial Leaf Blight" -> {
                    textView.text = "Hawar Daun"
                }
                "Bacterial Leaf Streak" -> {
                    textView.text = "Bercak Daun Bakteri Padi"
                }
                "Bacterial Panicle Blight" -> {
                    textView.text = "Busuk Bulir Bakteri"
                }
                "Blast" -> {
                    textView.text = "Blas"
                }
                "Brown Spot" -> {
                    textView.text = "Bercak Daun Coklat"
                }
                "Dead Heart" -> {
                    textView.text = "Kematian Tunas Padi"
                }
                "Down Mildew" -> {
                    textView.text = "Embun Tepung"
                }
                "Hispa" -> {
                    textView.text = "Hispa"
                }
                "Tungro" -> {
                    textView.text = "Tungro"
                }
                "angular_leaf_spot" -> {
                    textView.text = "Target Spot"
                }
                "bean_rust" -> {
                    textView.text = "Penyakit Karat"
                }
            }
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