package co.vik.jobvo.screens.fragments.notification

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import co.vik.jobvo.pojo.MyqrModel
import co.vik.jobvo.pojo.Notipojo
import co.vik.jobvo.screens.fragments.myqrcode.MyqrRepo
import com.google.gson.JsonObject

class NotiViewModel (application: Application) : AndroidViewModel(application) {
    private val data: MutableLiveData<Notipojo>
    private val notiRepo: NotiRepo

    init {
        data = MutableLiveData<Notipojo>()
        notiRepo = NotiRepo()
    }


    fun getNotiData(): MutableLiveData<Notipojo> {
        return data
    }

    fun getNotiData(submitdetail: JsonObject) {
        notiRepo.getNotiData(submitdetail, data)
    }



}