package co.vik.jobvo.screens.fragments.myqrcode

import android.util.Log
import androidx.lifecycle.MutableLiveData
import co.vik.jobvo.pojo.MyqrModel
import co.vik.jobvo.screens.dashboard.DashboardRepo
import com.example.contractapp.API.RetrofitAPI
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyqrRepo {

    companion object {
        var myqrRepo: MyqrRepo? = null
        var instance: MyqrRepo? = null
            get() {
                if (myqrRepo == null) myqrRepo = MyqrRepo()
                return myqrRepo
            }
    }

    fun getQrinfoData(
        submitdetail: JsonObject,
        data: MutableLiveData<MyqrModel>
    ) {
        try {
            val d: Call<MyqrModel> = RetrofitAPI.api.getmyinfo(submitdetail)
            d.enqueue(object : Callback<MyqrModel> {
                override fun onResponse(call: Call<MyqrModel>, response: Response<MyqrModel>) {
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

                override fun onFailure(call: Call<MyqrModel>, t: Throwable) {
                    Log.e("re", "" + t.toString())
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}