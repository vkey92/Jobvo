package co.vik.jobvo.screens.fragments.myqrcode

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import co.vik.jobvo.pojo.MyqrModel
import co.vik.jobvo.screens.dashboard.DashboardRepo
import com.google.gson.JsonObject

class MyqrViewModel (application: Application) : AndroidViewModel(application) {
    private val data: MutableLiveData<MyqrModel>
    private val myqrRepo: MyqrRepo

    init {
        data = MutableLiveData<MyqrModel>()
        myqrRepo = MyqrRepo()
    }


    fun getQrinfoData(): MutableLiveData<MyqrModel> {
        return data
    }

    fun getQrinfoData(submitdetail: JsonObject) {
        myqrRepo.getQrinfoData(submitdetail, data)
    }



}