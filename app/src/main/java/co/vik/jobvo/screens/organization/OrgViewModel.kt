package co.vik.jobvo.screens.organization

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import co.vik.jobvo.pojo.HistoryModel
import co.vik.jobvo.pojo.OrgainizationModel
import co.vik.jobvo.screens.history.HistoryRepository
import com.google.gson.JsonObject

class OrgViewModel (application: Application) : AndroidViewModel(application) {
    private val data: MutableLiveData<OrgainizationModel>
    private val orgRepository: OrgRepository

    init {
        data = MutableLiveData<OrgainizationModel>()
        orgRepository = OrgRepository()
    }


    fun getOrgData(): MutableLiveData<OrgainizationModel> {
        return data
    }

    fun getOrgData(submitdetail: JsonObject) {
        orgRepository.getOrgData(submitdetail, data)
    }



}