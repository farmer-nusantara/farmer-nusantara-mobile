package com.fahruaz.farmernusantara.response.auth

import com.google.gson.annotations.SerializedName

data class ChangeStatusAccountResponse(

	@field:SerializedName("secretCode")
	val secretCode: Int? = null
)
