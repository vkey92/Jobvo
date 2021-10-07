package co.vik.jobvo.pojo

import com.google.gson.annotations.SerializedName

data class ThankuModel(

	@field:SerializedName("response")
	val response: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
