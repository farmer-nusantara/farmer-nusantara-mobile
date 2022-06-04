package com.fahruaz.farmernusantara.response.plantdisease

import com.google.gson.annotations.SerializedName

data class GetSickPlantResponse(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("latitude")
	val latitude: Double? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("__v")
	val V: Int? = null,

	@field:SerializedName("farmland_id")
	val farmlandId: FarmlandId? = null,

	@field:SerializedName("picturedBy")
	val picturedBy: PicturedBy2? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("diseasePlant")
	val diseasePlant: String? = null,

	@field:SerializedName("longitude")
	val longitude: Double? = null
)

data class FarmlandId(

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

data class PicturedBy2(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("__v")
	val V: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
