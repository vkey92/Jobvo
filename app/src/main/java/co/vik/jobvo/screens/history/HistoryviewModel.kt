package co.vik.jobvo.screens.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import co.vik.jobvo.pojo.HistoryModel
import co.vik.jobvo.pojo.Otpmodel
import co.vik.jobvo.screens.signup.SignupRepository
import com.google.gson.JsonObject

class HistoryviewModel(application: Application) : AndroidViewModel(application) {
    private val data: MutableLiveData<HistoryModel>
    private val historyRepository: HistoryRepository

    init {
        data = MutableLiveData<HistoryModel>()
        historyRepository = HistoryRepository()
    }


    fun getHistoryData(): MutableLiveData<HistoryModel> {
        return data
    }

    fun getHistoryData(submitdetail: JsonObject) {
        historyRepository.getHistoryData(submitdetail, data)
    }



}