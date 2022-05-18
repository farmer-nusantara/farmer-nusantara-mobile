package com.fahruaz.farmernusantara.response.auth

import com.google.gson.annotations.SerializedName

data class SendTokenMessageResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataSecretCode(

	@field:SerializedName("secretCode")
	val secretCode: String? = null
)
