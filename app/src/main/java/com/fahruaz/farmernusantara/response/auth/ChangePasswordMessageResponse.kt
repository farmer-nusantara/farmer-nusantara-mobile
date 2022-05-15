package com.fahruaz.farmernusantara.response.auth

import com.google.gson.annotations.SerializedName

data class ChangePasswordMessageResponse(

	@field:SerializedName("message")
	val message: String? = null
)
