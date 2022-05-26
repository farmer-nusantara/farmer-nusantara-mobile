package com.fahruaz.farmernusantara.response.farmland

import com.google.gson.annotations.SerializedName

data class GetAllFarmlandByOwnerResponse(

	@field:SerializedName("GetAllFarmlandByOwnerResponse")
	val getAllFarmlandByOwnerResponse: List<GetAllFarmlandByOwnerResponseItem?>? = null
)

data class GetAllFarmlandByOwnerResponseItem(

	@field:SerializedName("farmName")
	val farmName: String? = null,

	@field:SerializedName("markColor")
	val markColor: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("plantType")
	val plantType: String? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null
)
