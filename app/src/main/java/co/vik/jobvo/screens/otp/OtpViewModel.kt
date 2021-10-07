package co.vik.jobvo.screens.otp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import co.vik.jobvo.pojo.Otpmodel
import co.vik.jobvo.pojo.RegisterModel
import co.vik.jobvo.pojo.ResendOtpModel

import com.google.gson.JsonObject

class OtpViewModel (application: Application) : AndroidViewModel(application) {
    private val data: MutableLiveData<Otpmodel>
    private val regiserData: MutableLiveData<RegisterModel>
    private val resendData: MutableLiveData<ResendOtpModel>
    private val otpRepository: OtpRepository

    init {
        data = MutableLiveData<Otpmodel>()
        regiserData = MutableLiveData<RegisterModel>()
        resendData = MutableLiveData<ResendOtpModel>()
        otpRepository = OtpRepository()
    }


    fun getOtpData(): MutableLiveData<Otpmodel> {
        return data
    }

    fun getRegisterData(): MutableLiveData<RegisterModel> {
        return regiserData
    }

    fun getResendData(): MutableLiveData<ResendOtpModel> {
        return resendData
    }




    fun getOtpData(submitdetail: JsonObject) {
        otpRepository.getOtpData(submitdetail, data)
    }

    fun getRegisterData(submitdetail: JsonObject) {
        otpRepository.getRegisterData(submitdetail, regiserData)
    }

    fun getResendData(submitdetail: JsonObject) {
        otpRepository.getResendData(submitdetail, resendData)
    }



}