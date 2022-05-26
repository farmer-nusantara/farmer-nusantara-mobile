package com.fahruaz.farmernusantara.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fahruaz.farmernusantara.R
import com.fahruaz.farmernusantara.databinding.FarmlandItemBinding
import com.fahruaz.farmernusantara.response.farmland.GetAllFarmlandByOwnerResponseItem

class FarmlandsAdapter(
    private var farmlands: List<GetAllFarmlandByOwnerResponseItem>): RecyclerView.Adapter<FarmlandsAdapter.MyViewHolder>() {

//    private var farmlands = emptyList<GetAllFarmlandByOwnerResponseItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentResult = farmlands[position]
        holder.bind(currentResult)
    }

    override fun getItemCount(): Int {
        return farmlands.size
    }

//    fun setData(newData: List<GetAllFarmlandByOwnerResponseItem>) {
//        val farmlandsDiffUtil = FarmlandsDiffUtil(farmlands, newData)
//        val diffUtilResult = DiffUtil.calculateDiff(farmlandsDiffUtil)
//        farmlands = newData
//        diffUtilResult.dispatchUpdatesTo(this)
//    }

    class MyViewHolder(private val binding: FarmlandItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(result: GetAllFarmlandByOwnerResponseItem) {
            binding.result = result
            binding.executePendingBindings()
            val hexColorToInt = Color.parseColor(result.markColor)
            binding.ivFarmlandColor.setColorFilter(hexColorToInt)
            Glide.with(itemView.context)
                .load(result.imageUrl)
                .placeholder(R.drawable.image_default)
                .into(binding.ivFarmland)
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val binding = FarmlandItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return MyViewHolder(binding)
            }
        }
    }

}