package com.fahruaz.farmernusantara.response.auth

import com.google.gson.annotations.SerializedName

data class TokenActivationResponse(

	@field:SerializedName("secretCode")
	val secretCode: SecretCode? = null
)

data class SecretCode(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("dateCreated")
	val dateCreated: String? = null,

	@field:SerializedName("__v")
	val V: Int? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
