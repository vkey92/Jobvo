package co.vik.jobvo.screens.scanhistory

import android.util.Log
import androidx.lifecycle.MutableLiveData
import co.vik.jobvo.pojo.OrgainizationModel
import co.vik.jobvo.pojo.ScannedModel
import co.vik.jobvo.screens.organization.OrgRepository
import com.example.contractapp.API.RetrofitAPI
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScanRepository {
    companion object {
        var scanRepository: ScanRepository? = null
        var instance: ScanRepository? = null
            get() {
                if (scanRepository == null) scanRepository = ScanRepository()
                return scanRepository
            }
    }

    fun getScanData(
        submitdetail: JsonObject,
        data: MutableLiveData<ScannedModel>
    ) {
        try {
            val d: Call<ScannedModel> = RetrofitAPI.api.getscandata(submitdetail)
            d.enqueue(object : Callback<ScannedModel> {
                override fun onResponse(call: Call<ScannedModel>, response: Response<ScannedModel>) {
                    try {
                        Log.e("response code = ", "" + response.code())
                        if(response.code() == 200 || response.code() == 201)
                            data.postValue(response.body())
                        else
                            data.postValue(null)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ScannedModel>, t: Throwable) {
                    Log.e("re", "" + t.toString())
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}