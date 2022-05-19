package com.fahruaz.farmernusantara.response.auth

import com.google.gson.annotations.SerializedName

data class RegisterMessageResponse(

	@field:SerializedName("data")
	val data: DataRegister? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataRegister(

	@field:SerializedName("userId")
	val userId: String? = null
)
