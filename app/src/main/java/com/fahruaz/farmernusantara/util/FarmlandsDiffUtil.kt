package com.fahruaz.farmernusantara.util

import androidx.recyclerview.widget.DiffUtil
import com.fahruaz.farmernusantara.response.farmland.GetAllFarmlandByOwnerResponseItem

class FarmlandsDiffUtil(
    private val oldList: List<GetAllFarmlandByOwnerResponseItem>,
    private val newList: List<GetAllFarmlandByOwnerResponseItem>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}