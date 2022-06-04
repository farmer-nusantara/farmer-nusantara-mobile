package com.fahruaz.farmernusantara.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fahruaz.farmernusantara.databinding.DiseaseItemBinding
import com.fahruaz.farmernusantara.response.farmland.SickPlantsItem
import com.fahruaz.farmernusantara.util.DiseasesDiffUtil

class DiseaseAdapter(
    private val detailListener: (id: String) -> Unit
) : RecyclerView.Adapter<DiseaseAdapter.MyViewHolder>()
{

    private var diseases = emptyList<SickPlantsItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentResult = diseases[position]
        holder.bind(currentResult)
        holder.ivDiseaseImage.setOnClickListener {
            Log.e("ID disease", currentResult.id!!)
            detailListener.invoke(currentResult.id!!)
        }
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