package co.vik.jobvo.screens.forgotpass

import android.util.Log
import androidx.lifecycle.MutableLiveData
import co.vik.jobvo.pojo.Forgotmodel
import co.vik.jobvo.pojo.Loginmodel
import com.example.contractapp.API.RetrofitAPI
import com.example.qdischyd.activity.iqc.LoginRepository
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotRepository {

    companion object {
        var forgotRepository: ForgotRepository? = null
        var instance: ForgotRepository? = null
            get() {
                if (forgotRepository == null) forgotRepository = ForgotRepository()
                return forgotRepository
            }
    }

    fun geForgotData(
        submitdetail: JsonObject,
        data: MutableLiveData<Forgotmodel>
    ) {
        try {
            val d: Call<Forgotmodel> = RetrofitAPI.api.forgotpass(submitdetail)
            d.enqueue(object : Callback<Forgotmodel> {
                override fun onResponse(call: Call<Forgotmodel>, response: Response<Forgotmodel>) {
                    try {
                        Log.e("response code = ", "" + response.code())
                        if (response.code() == 200 || response.code() == 201)
                            data.postValue(response.body())
                        else
                            data.postValue(null)


                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<Forgotmodel>, t: Throwable) {
                    Log.e("re", "" + t.toString())
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}