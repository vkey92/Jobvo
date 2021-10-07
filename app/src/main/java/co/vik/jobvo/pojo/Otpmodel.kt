package co.vik.jobvo.pojo

import com.google.gson.annotations.SerializedName

 class Otpmodel(

	@field:SerializedName("data")
	val data: getOtpData? = null,

	@field:SerializedName("response")
	val response: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

 class getOtpData(

	@field:SerializedName("otp_verify")
	val otpVerify: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("mobile")
	val mobile: String? = null
)
