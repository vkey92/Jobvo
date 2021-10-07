package co.vik.jobvo.screens.history

import android.util.Log
import androidx.lifecycle.MutableLiveData
import co.vik.jobvo.pojo.HistoryModel
import co.vik.jobvo.pojo.Otpmodel
import co.vik.jobvo.screens.signup.SignupRepository
import com.example.contractapp.API.RetrofitAPI
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryRepository  {

    companion object {
        var historyRepository: HistoryRepository? = null
        var instance: HistoryRepository? = null
            get() {
                if (historyRepository == null) historyRepository = HistoryRepository()
                return historyRepository
            }
    }

    fun getHistoryData(
        submitdetail: JsonObject,
        data: MutableLiveData<HistoryModel>
    ) {
        try {
            val d: Call<HistoryModel> = RetrofitAPI.api.gethistory(submitdetail)
            d.enqueue(object : Callback<HistoryModel> {
                override fun onResponse(call: Call<HistoryModel>, response: Response<HistoryModel>) {
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

                override fun onFailure(call: Call<HistoryModel>, t: Throwable) {
                    Log.e("re", "" + t.toString())
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}