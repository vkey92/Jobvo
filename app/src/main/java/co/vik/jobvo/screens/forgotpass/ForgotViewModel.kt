package co.vik.jobvo.screens.forgotpass

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import co.vik.jobvo.pojo.Forgotmodel
import co.vik.jobvo.pojo.Loginmodel
import com.example.qdischyd.activity.iqc.LoginRepository
import com.google.gson.JsonObject

class ForgotViewModel (application: Application) : AndroidViewModel(application) {
    private val data: MutableLiveData<Forgotmodel>
    private val forgotRepository: ForgotRepository

    init {
        data = MutableLiveData<Forgotmodel>()
        forgotRepository = ForgotRepository()
    }


    fun getForgotData(): MutableLiveData<Forgotmodel> {
        return data
    }

    fun getForgotData(submitdetail: JsonObject) {
        forgotRepository.geForgotData(submitdetail, data)
    }



}