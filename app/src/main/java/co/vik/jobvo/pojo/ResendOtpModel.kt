package co.vik.jobvo.pojo

import com.google.gson.annotations.SerializedName

data class ResendOtpModel(

	@field:SerializedName("data")
	val data: getResendData? = null,

	@field:SerializedName("response")
	val response: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class getResendData(

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)
