package co.vik.jobvo.screens.signup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import co.vik.jobvo.pojo.Loginmodel
import co.vik.jobvo.pojo.Otpmodel
import com.example.qdischyd.activity.iqc.LoginRepository
import com.google.gson.JsonObject

class SignupViewModel (application: Application) : AndroidViewModel(application) {
    private val data: MutableLiveData<Otpmodel>
    private val signupRepository: SignupRepository

    init {
        data = MutableLiveData<Otpmodel>()
        signupRepository = SignupRepository()
    }


    fun getOtpData(): MutableLiveData<Otpmodel> {
        return data
    }

    fun getOtpData(submitdetail: JsonObject) {
        signupRepository.getOtpData(submitdetail, data)
    }



}