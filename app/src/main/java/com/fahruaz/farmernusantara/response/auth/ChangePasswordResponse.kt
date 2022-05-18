package com.fahruaz.farmernusantara.response.auth

import com.google.gson.annotations.SerializedName

data class ChangePasswordResponse(

	@field:SerializedName("passwordConfirmation")
	val passwordConfirmation: String? = null,

	@field:SerializedName("newPassword")
	val newPassword: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
