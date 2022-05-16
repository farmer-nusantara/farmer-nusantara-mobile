package com.fahruaz.farmernusantara.response.auth

import com.google.gson.annotations.SerializedName

data class SendTokenResponse(

	@field:SerializedName("email")
	val email: String? = null
)
