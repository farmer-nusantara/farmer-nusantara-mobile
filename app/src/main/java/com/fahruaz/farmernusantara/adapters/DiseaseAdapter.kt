package com.fahruaz.farmernusantara.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fahruaz.farmernusantara.databinding.DiseaseItemBinding
import com.fahruaz.farmernusantara.response.farmland.SickPlantsItem
import com.fahruaz.farmernusantara.response.plantdisease.GetAllSickPlantsResponseItem
import com.fahruaz.farmernusantara.util.DiseasesDiffUtil

class DiseaseAdapter : RecyclerView.Adapter<DiseaseAdapter.MyViewHolder>() {

    private var diseases = emptyList<SickPlantsItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentResult = diseases[position]
        holder.bind(currentResult)
//        holder.ivDiseaseImage.setOnClickListener {
//            detailListener.invoke(currentResult.id!!)
//        }
    }

    override fun getItemCount(): Int {
        return diseases.size
    }

    fun setData(newData: List<SickPlantsItem>) {
        val farmlandsDiffUtil = DiseasesDiffUtil(diseases, newData)
        val diffUtilResult = DiffUtil.calculateDiff(farmlandsDiffUtil)
        diseases = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }

    class MyViewHolder(private val binding: DiseaseItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(result: SickPlantsItem) {
            binding.result = result
            binding.executePendingBindings()
//            val hexColorToInt = Color.parseColor(result.markColor)
//            binding.ivFarmlandColor.setColorFilter(hexColorToInt)
//            Glide.with(itemView.context)
//                .load(result.imageUrl)
//                .placeholder(R.drawable.image_default)
//                .into(binding.ivFarmland)
        }

        var ivDiseaseImage = binding.ivDiseaseImage

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val binding = DiseaseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return MyViewHolder(binding)
            }
        }
    }

}