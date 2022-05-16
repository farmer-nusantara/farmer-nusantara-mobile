package com.fahruaz.farmernusantara.response.auth

import com.google.gson.annotations.SerializedName

data class SendTokenResetPasswordResponse(

	@field:SerializedName("email")
	val email: String? = null
)
