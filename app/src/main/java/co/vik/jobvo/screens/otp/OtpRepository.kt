package co.vik.jobvo.screens.otp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import co.vik.jobvo.pojo.Otpmodel
import co.vik.jobvo.pojo.RegisterModel
import co.vik.jobvo.pojo.ResendOtpModel
import com.example.contractapp.API.RetrofitAPI
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpRepository {

    companion object {
        var otpRepository: OtpRepository? = null
        var instance: OtpRepository? = null
            get() {
                if (otpRepository == null) otpRepository = OtpRepository()
                return otpRepository
            }
    }

    fun getOtpData(
            submitdetail: JsonObject,
            data: MutableLiveData<Otpmodel>
    ) {
        try {
            val d: Call<Otpmodel> = RetrofitAPI.api.otpverify(submitdetail)
            d.enqueue(object : Callback<Otpmodel> {
                override fun onResponse(call: Call<Otpmodel>, response: Response<Otpmodel>) {
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

                override fun onFailure(call: Call<Otpmodel>, t: Throwable) {
                    Log.e("re", "" + t.toString())
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getRegisterData(
            submitdetail: JsonObject,
            data: MutableLiveData<RegisterModel>) {
        try {
            val d: Call<RegisterModel> = RetrofitAPI.api.newregister(submitdetail)
            d.enqueue(object : Callback<RegisterModel> {
                override fun onResponse(call: Call<RegisterModel>, response: Response<RegisterModel>) {
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

                override fun onFailure(call: Call<RegisterModel>, t: Throwable) {
                    Log.e("re", "" + t.toString())
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getResendData(
            submitdetail: JsonObject,
            data: MutableLiveData<ResendOtpModel>) {
        try {
            val d: Call<ResendOtpModel> = RetrofitAPI.api.resendotp(submitdetail)
            d.enqueue(object : Callback<ResendOtpModel> {
                override fun onResponse(call: Call<ResendOtpModel>, response: Response<ResendOtpModel>) {
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

                override fun onFailure(call: Call<ResendOtpModel>, t: Throwable) {
                    Log.e("re", "" + t.toString())
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}