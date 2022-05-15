package com.fahruaz.farmernusantara.response.auth

import com.google.gson.annotations.SerializedName

data class CheckTokenResetMessageResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataTokenReset(

	@field:SerializedName("email")
	val email: String? = null
)
