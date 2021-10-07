package co.vik.jobvo.pojo

import com.google.gson.annotations.SerializedName

class ScannedModel {
    @SerializedName("data")
    val data: List<DataItem>? = null

    @SerializedName("response")
    val response: String? = null

    @SerializedName("status")
    val status: String? = null

    inner class DataItem {
        @SerializedName("batch_number")
        val batchNumber: String? = null

        @SerializedName("scan_status")
        val scanStatus: String? = null

        @SerializedName("user_name")
        val userName: String? = null

        @SerializedName("scan_user_id")
        val scanUserId: String? = null

        @SerializedName("approved_status")
        val approvedStatus: String? = null

        @SerializedName("reson_for_disapproved")
        val resonForDisapproved: String? = null

        @SerializedName("id")
        val id: String? = null

        @SerializedName("scan_time")
        val scanTime: String? = null

        @SerializedName("scan_date")
        val scanDate: String? = null
    }
}