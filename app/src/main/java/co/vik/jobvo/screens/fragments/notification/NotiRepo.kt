package co.vik.jobvo.screens.fragments.notification

import android.util.Log
import androidx.lifecycle.MutableLiveData
import co.vik.jobvo.pojo.MyqrModel
import co.vik.jobvo.pojo.Notipojo
import co.vik.jobvo.screens.fragments.myqrcode.MyqrRepo
import com.example.contractapp.API.RetrofitAPI
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotiRepo {

    companion object {
        var notiRepo: NotiRepo? = null
        var instance: NotiRepo? = null
            get() {
                if (notiRepo == null) notiRepo = NotiRepo()
                return notiRepo
            }
    }

    fun getNotiData(
        submitdetail: JsonObject,
        data: MutableLiveData<Notipojo>
    ) {
        try {
            val d: Call<Notipojo> = RetrofitAPI.api.getnotification(submitdetail)
            d.enqueue(object : Callback<Notipojo> {
                override fun onResponse(call: Call<Notipojo>, response: Response<Notipojo>) {
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

                override fun onFailure(call: Call<Notipojo>, t: Throwable) {
                    Log.e("re", "" + t.toString())
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}