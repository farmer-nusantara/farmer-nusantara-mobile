package com.fahruaz.farmernusantara.response.farmland

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.io.Serializable

@Parcelize
data class ShowFarmlandDetailResponse(

	@field:SerializedName("farmName")
	val farmName: String? = null,

	@field:SerializedName("owner")
	val owner: @RawValue Owner? = null,

	@field:SerializedName("markColor")
	val markColor: String? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("sickPlants")
	val sickPlants: @RawValue List<SickPlantsItem?>? = null,

	@field:SerializedName("__v")
	val V: Int? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("plantType")
	val plantType: String? = null
) : Parcelable

data class SickPlantsItem(

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
) : Serializable

data class Owner(

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
) : Serializable
