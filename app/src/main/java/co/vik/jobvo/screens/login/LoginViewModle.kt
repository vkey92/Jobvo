package com.example.qdischyd.activity.iqc

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import co.vik.jobvo.pojo.Loginmodel
import com.google.gson.JsonObject


class LoginViewModle(application: Application) : AndroidViewModel(application) {
    private val data: MutableLiveData<Loginmodel>
    private val loginRepository: LoginRepository

    init {
        data = MutableLiveData<Loginmodel>()
        loginRepository = LoginRepository()
    }


    fun getLoginData(): MutableLiveData<Loginmodel> {
        return data
    }

    fun getLoginData(submitdetail: JsonObject) {
        loginRepository.getLoginData(submitdetail, data)
    }



}