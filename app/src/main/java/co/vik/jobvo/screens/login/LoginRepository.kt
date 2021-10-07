package com.example.qdischyd.activity.iqc

import android.util.Log
import androidx.lifecycle.MutableLiveData
import co.vik.jobvo.pojo.Loginmodel
import com.example.contractapp.API.RetrofitAPI
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository {

    companion object {
        var loginRepository: LoginRepository? = null
        var instance: LoginRepository? = null
            get() {
                if (loginRepository == null) loginRepository = LoginRepository()
                return loginRepository
            }
    }

    fun getLoginData(
        submitdetail: JsonObject,
        data: MutableLiveData<Loginmodel>
    ) {
        try {
            val d: Call<Loginmodel> = RetrofitAPI.api.login(submitdetail)
            d.enqueue(object : Callback<Loginmodel> {
                override fun onResponse(call: Call<Loginmodel>, response: Response<Loginmodel>) {
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

                override fun onFailure(call: Call<Loginmodel>, t: Throwable) {
                    Log.e("re", "" + t.toString())
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}