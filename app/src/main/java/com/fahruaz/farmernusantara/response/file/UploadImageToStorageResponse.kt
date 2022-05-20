package com.fahruaz.farmernusantara.response.file

import com.google.gson.annotations.SerializedName

data class UploadImageToStorageResponse(

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
