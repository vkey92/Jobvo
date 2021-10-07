package co.vik.jobvo.screens.signup

import android.util.Log
import androidx.lifecycle.MutableLiveData
import co.vik.jobvo.pojo.Otpmodel
import co.vik.jobvo.pojo.RegisterModel
import co.vik.jobvo.pojo.ResendOtpModel
import co.vik.jobvo.screens.otp.OtpRepository
import com.example.contractapp.API.RetrofitAPI
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupRepository {

    companion object {
        var signupRepository: SignupRepository? = null
        var instance: SignupRepository? = null
            get() {
                if (signupRepository == null) signupRepository = SignupRepository()
                return signupRepository
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


}