package com.fahruaz.farmernusantara.response.plantdisease

import com.google.gson.annotations.SerializedName

data class SaveDiseasePlantResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Data(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("latitude")
	val latitude: Double? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("__v")
	val V: Int? = null,

	@field:SerializedName("farmland_id")
	val farmlandId: String? = null,

	@field:SerializedName("picturedBy")
	val picturedBy: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("diseasePlant")
	val diseasePlant: String? = null,

	@field:SerializedName("longitude")
	val longitude: Double? = null
)
