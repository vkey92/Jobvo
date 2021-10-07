package co.vik.jobvo.pojo

import com.google.gson.annotations.SerializedName

class HistoryModel {
	@SerializedName("data")
	val data: List<DataItem?>? = null

	@SerializedName("response")
	val response: String? = null

	@SerializedName("status")
	val status: String? = null

	 class DataItem {
		@SerializedName("date")
		val date: String? = null

		@SerializedName("user_id")
		val userId: String? = null

		@SerializedName("download_type")
		val downloadType: String? = null

		@SerializedName("totalprice")
		val totalprice: String? = null

		@SerializedName("download_path")
		val downloadPath: String? = null

		@SerializedName("id")
		val id: String? = null

		@SerializedName("card_type")
		val cardType: String? = null

		@SerializedName("id_card_ganrate_status")
		val idCardGanrateStatus: String? = null

		@SerializedName("id_card_msg")
		val idCardMsg: String? = null
	}
}