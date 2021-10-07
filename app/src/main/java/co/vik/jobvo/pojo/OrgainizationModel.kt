package co.vik.jobvo.pojo

import com.google.gson.annotations.SerializedName

class OrgainizationModel {
	@SerializedName("data")
	val data: List<DataItem>? = null

	@SerializedName("response")
	val response: String? = null

	@SerializedName("status")
	val status: String? = null

	inner class DataItem {
		@SerializedName("org_user_id")
		val orgUserId: String? = null

		@SerializedName("user_id")
		val userId: String? = null

		@SerializedName("name")
		val name: String? = null

		@SerializedName("id")
		val id: String? = null
	}
}