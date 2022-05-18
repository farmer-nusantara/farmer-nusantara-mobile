package com.fahruaz.farmernusantara.response.auth

import com.google.gson.annotations.SerializedName

data class TokenResetPasswordResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Data(

	@field:SerializedName("secretCode")
	val secretCode: String? = null
)
