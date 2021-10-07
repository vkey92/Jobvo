package co.vik.jobvo.pojo

import com.google.gson.annotations.SerializedName

class Notipojo {
    @SerializedName("data")
    val data: List<NotiDataItem>? = null

    @SerializedName("user_id")
    val userId: String? = null

    @SerializedName("response")
    val response: String? = null

    @SerializedName("status")
    val status: String? = null

    @SerializedName("total_record")
    val totalRecord = 0

    inner class NotiDataItem {
        @SerializedName("result")
        val result: String? = null

        @SerializedName("image")
        val image: String? = null

        @SerializedName("date_time")
        val dateTime: String? = null

        @SerializedName("id")
        val id: String? = null

        @SerializedName("title")
        val title: String? = null
    }
}