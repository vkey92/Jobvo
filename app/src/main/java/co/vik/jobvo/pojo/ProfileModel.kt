package co.vik.jobvo.pojo

import androidx.room.Entity
import com.google.gson.annotations.SerializedName


data class ProfileModel(

	@field:SerializedName("data")
	val data: getProfileData? = null,

	@field:SerializedName("response")
	val response: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) {

	inner class getProfileData(

			@field:SerializedName("pincode")
			val pincode: String? = null,

			@field:SerializedName("image")
			val image: String? = null,

			@field:SerializedName("address")
			val address: String? = null,

			@field:SerializedName("gender")
			val gender: String? = null,

			@field:SerializedName("profile_url")
			val profileUrl: String? = null,

			@field:SerializedName("city")
			val city: String? = null,

			@field:SerializedName("mobile")
			val mobile: String? = null,

			@field:SerializedName("distict")
			val distict: String? = null,


			@field:SerializedName("user_id")
			val userId: String? = null,

			@field:SerializedName("dob")
			val dob: String? = null,

			@field:SerializedName("name")
			val name: String? = null,

			@field:SerializedName("id")
			val id: String? = null,

			@field:SerializedName("state")
			val state: String? = null,

			@field:SerializedName("email")
			val email: String? = null
	)
}
