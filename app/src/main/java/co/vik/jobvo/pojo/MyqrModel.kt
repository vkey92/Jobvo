package co.vik.jobvo.pojo

import com.google.gson.annotations.SerializedName

data class MyqrModel(

	@field:SerializedName("batch_number")
	val batchNumber: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("response")
	val response: String? = null,

	@field:SerializedName("user_name")
	val userName: String? = null,

	@field:SerializedName("personal_text")
	val personalText: String? = null,

	@field:SerializedName("qr_code")
	val qrCode: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
