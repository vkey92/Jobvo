package co.vik.jobvo.screens.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import co.vik.jobvo.pojo.Otpmodel
import co.vik.jobvo.pojo.ProfileModel
import co.vik.jobvo.screens.signup.SignupRepository
import com.example.contractapp.API.RetrofitAPI
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileRepository {
    companion object {
        var profileRepository: ProfileRepository? = null
        var instance: ProfileRepository? = null
            get() {
                if (profileRepository == null) profileRepository = ProfileRepository()
                return profileRepository
            }
    }

    fun getProfileData(part: MultipartBody.Part,useridpart: MultipartBody.Part, namepart: MultipartBody.Part, dobpart: MultipartBody.Part, genderpart: MultipartBody.Part, phonepart: MultipartBody.Part, emailpart: MultipartBody.Part,
        citypart: MultipartBody.Part, statepart: MultipartBody.Part, distpart: MultipartBody.Part, addpart: MultipartBody.Part, pinpart: MultipartBody.Part, profilepart: MultipartBody.Part,
        data: MutableLiveData<ProfileModel>
    ) {
        try {
            val d: Call<ProfileModel> = RetrofitAPI.api.updateprofilepic(part,useridpart,namepart,dobpart,genderpart,phonepart,emailpart,citypart,statepart,
                                     distpart,addpart,pinpart,profilepart)
            d.enqueue(object : Callback<ProfileModel> {
                override fun onResponse(call: Call<ProfileModel>, response: Response<ProfileModel>) {
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

                override fun onFailure(call: Call<ProfileModel>, t: Throwable) {
                    Log.e("re", "" + t.toString())
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}