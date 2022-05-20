package com.fahruaz.farmernusantara.response.farmland

import com.google.gson.annotations.SerializedName

data class CreateFarmlandResponse(

	@field:SerializedName("farmName")
	val farmName: String? = null,

	@field:SerializedName("owner")
	val owner: String? = null,

	@field:SerializedName("markColor")
	val markColor: String? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("__v")
	val V: Int? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("plantType")
	val plantType: String? = null
)
