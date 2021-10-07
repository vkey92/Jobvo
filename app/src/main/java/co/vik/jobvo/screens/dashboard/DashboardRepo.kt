package co.vik.jobvo.screens.dashboard

import android.util.Log
import androidx.lifecycle.MutableLiveData
import co.vik.jobvo.pojo.HistoryModel
import co.vik.jobvo.screens.history.HistoryRepository
import com.example.contractapp.API.RetrofitAPI
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardRepo {
    companion object {
        var dashboardRepo: DashboardRepo? = null
        var instance: DashboardRepo? = null
            get() {
                if (dashboardRepo == null) dashboardRepo = DashboardRepo()
                return dashboardRepo
            }
    }

    fun getScanData(
        submitdetail: JsonObject,
        data: MutableLiveData<JsonObject>
    ) {
        try {
            val d: Call<JsonObject> = RetrofitAPI.api.insertscan(submitdetail)
            d.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    try {
                        Log.e("response code = ", "" + response.code())
                        if(response.code() == 200 || response.code() == 201)
                            data.postValue(response.body())
                        else
                            data.postValue(null)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("re", "" + t.toString())
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}