package com.fahruaz.farmernusantara.databindingadapters

import android.graphics.Color
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.fahruaz.farmernusantara.R

class EditFarmlandBinding {

    companion object {

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {
            imageView.load(imageUrl) {
                crossfade(600)
                error(R.drawable.image_default)
            }
        }

        @BindingAdapter("setFarmlandName")
        @JvmStatic
        fun setFarmlandName(editText: EditText, name: String) {
            editText.setText(name)
        }

        @BindingAdapter("setFarmlandLocation")
        @JvmStatic
        fun setFarmlandLocation(editText: EditText, name: String) {
            editText.setText(name)
        }

        @BindingAdapter("setPlantType")
        @JvmStatic
        fun setPlantType(autoCompleteTextView: AutoCompleteTextView, plant: String) {
            autoCompleteTextView.setText(plant, false)
        }

        @BindingAdapter("loadFarmlandColor")
        @JvmStatic
        fun loadFarmlandColor(imageView: ImageView, markColor: String) {
            val hexColorToInt = Color.parseColor(markColor)
            imageView.setColorFilter(hexColorToInt)
        }

    }

}