package com.fahruaz.farmernusantara.response.auth

import com.google.gson.annotations.SerializedName

data class ChangeStatusAccountMessageResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataStatusAccount(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
