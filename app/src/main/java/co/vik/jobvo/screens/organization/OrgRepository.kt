package co.vik.jobvo.screens.organization

import android.util.Log
import androidx.lifecycle.MutableLiveData
import co.vik.jobvo.pojo.HistoryModel
import co.vik.jobvo.pojo.OrgainizationModel
import co.vik.jobvo.screens.history.HistoryRepository
import com.example.contractapp.API.RetrofitAPI
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrgRepository {

    companion object {
        var orgRepository: OrgRepository? = null
        var instance: OrgRepository? = null
            get() {
                if (orgRepository == null) orgRepository = OrgRepository()
                return orgRepository
            }
    }

    fun getOrgData(
        submitdetail: JsonObject,
        data: MutableLiveData<OrgainizationModel>
    ) {
        try {
            val d: Call<OrgainizationModel> = RetrofitAPI.api.getorgainization(submitdetail)
            d.enqueue(object : Callback<OrgainizationModel> {
                override fun onResponse(call: Call<OrgainizationModel>, response: Response<OrgainizationModel>) {
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

                override fun onFailure(call: Call<OrgainizationModel>, t: Throwable) {
                    Log.e("re", "" + t.toString())
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}