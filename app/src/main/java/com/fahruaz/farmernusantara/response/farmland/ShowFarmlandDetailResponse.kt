package com.fahruaz.farmernusantara.response.farmland

import com.google.gson.annotations.SerializedName

data class ShowFarmlandDetailResponse(

	@field:SerializedName("farmName")
	val farmName: String? = null,

	@field:SerializedName("owner")
	val owner: Owner? = null,

	@field:SerializedName("markColor")
	val markColor: String? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("sickPlants")
	val sickPlants: List<SickPlantsItem?>? = null,

	@field:SerializedName("__v")
	val V: Int? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("plantType")
	val plantType: String? = null
)

data class SickPlantsItem(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("coordinate")
	val coordinate: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("diseasePlant")
	val diseasePlant: String? = null
)

data class Owner(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
