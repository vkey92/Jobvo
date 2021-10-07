package co.vik.jobvo.screens.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import co.vik.jobvo.pojo.HistoryModel
import co.vik.jobvo.screens.history.HistoryRepository
import com.google.gson.JsonObject

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val data: MutableLiveData<JsonObject>
    private val dashboardRepo: DashboardRepo

    init {
        data = MutableLiveData<JsonObject>()
        dashboardRepo = DashboardRepo()
    }


    fun getScanData(): MutableLiveData<JsonObject> {
        return data
    }

    fun getScanData(submitdetail: JsonObject) {
        dashboardRepo.getScanData(submitdetail, data)
    }



}