package co.vik.jobvo.screens.webview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import co.vik.jobvo.pojo.Otpmodel
import co.vik.jobvo.pojo.ThankuModel
import com.google.gson.JsonObject

class HelpViewModle (application: Application) : AndroidViewModel(application) {
    private val data: MutableLiveData<ThankuModel>
    private val helpRepository: HelpRepository

    init {
        data = MutableLiveData<ThankuModel>()
        helpRepository = HelpRepository()
    }


    fun getThankuData(): MutableLiveData<ThankuModel> {
        return data
    }

    fun getThankuData(submitdetail: JsonObject) {
        helpRepository.getThankuData(submitdetail, data)
    }



}