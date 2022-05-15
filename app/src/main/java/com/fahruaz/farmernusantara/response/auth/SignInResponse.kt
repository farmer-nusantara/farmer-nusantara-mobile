package com.fahruaz.farmernusantara.response.auth

import com.google.gson.annotations.SerializedName

data class SignInResponse(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
