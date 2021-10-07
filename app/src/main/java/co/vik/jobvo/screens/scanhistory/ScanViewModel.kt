package co.vik.jobvo.screens.scanhistory

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import co.vik.jobvo.pojo.OrgainizationModel
import co.vik.jobvo.pojo.ScannedModel
import co.vik.jobvo.screens.organization.OrgRepository
import com.google.gson.JsonObject

class ScanViewModel  (application: Application) : AndroidViewModel(application) {
    private val data: MutableLiveData<ScannedModel>
    private val scanRepository: ScanRepository

    init {
        data = MutableLiveData<ScannedModel>()
        scanRepository = ScanRepository()
    }


    fun getScanData(): MutableLiveData<ScannedModel> {
        return data
    }

    fun getScanData(submitdetail: JsonObject) {
        scanRepository.getScanData(submitdetail, data)
    }



}