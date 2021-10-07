package co.vik.jobvo.screens.webview

import android.util.Log
import androidx.lifecycle.MutableLiveData
import co.vik.jobvo.pojo.Otpmodel
import co.vik.jobvo.pojo.ThankuModel
import com.example.contractapp.API.RetrofitAPI
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HelpRepository {
    companion object {
        var helpRepository: HelpRepository? = null
        var instance: HelpRepository? = null
            get() {
                if (helpRepository == null) helpRepository = HelpRepository()
                return helpRepository
            }
    }

    fun getThankuData(
        submitdetail: JsonObject,
        data: MutableLiveData<ThankuModel>
    ) {
        try {
            val d: Call<ThankuModel> = RetrofitAPI.api.thankuorder(submitdetail)
            d.enqueue(object : Callback<ThankuModel> {
                override fun onResponse(call: Call<ThankuModel>, response: Response<ThankuModel>) {
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

                override fun onFailure(call: Call<ThankuModel>, t: Throwable) {
                    Log.e("re", "" + t.toString())
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}