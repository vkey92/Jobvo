package co.vik.jobvo.pojo

import com.google.gson.annotations.SerializedName

data class Loginmodel(

	@field:SerializedName("data")
	val data: getLoginData? = null,

	@field:SerializedName("response")
	val response: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class getLoginData(

	@field:SerializedName("dashboard_main_title")
	val dashboardMainTitle: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("profile_id")
	val profileId: String? = null,

	@field:SerializedName("mobile")
	val mobile: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("visitingcard_amount")
	val visitingcardAmount: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("idcard_amount")
	val idcardAmount: String? = null,

	@field:SerializedName("qr_code_amount")
	val qrCodeAmount: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("whatsapp_number")
	val whatsappNumber: String? = null
)
