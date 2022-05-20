package com.fahruaz.farmernusantara.response.profile

import com.google.gson.annotations.SerializedName

data class EditProfileResponse(

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("name")
	val name: String? = null
)
