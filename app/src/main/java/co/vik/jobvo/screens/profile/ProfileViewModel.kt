package co.vik.jobvo.screens.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import co.vik.jobvo.pojo.ProfileModel
import com.google.gson.JsonObject
import okhttp3.MultipartBody

class ProfileViewModel (application: Application) : AndroidViewModel(application) {
    private val data: MutableLiveData<ProfileModel>
    private val profileRepository: ProfileRepository

    init {
        data = MutableLiveData<ProfileModel>()
        profileRepository = ProfileRepository()
    }


    fun getProfileData(): MutableLiveData<ProfileModel> {
        return data
    }

    fun getProfileData(part: MultipartBody.Part,useridpart: MultipartBody.Part,namepart: MultipartBody.Part,dobpart: MultipartBody.Part,genderpart: MultipartBody.Part,phonepart: MultipartBody.Part,emailpart: MultipartBody.Part,
                       citypart: MultipartBody.Part,statepart: MultipartBody.Part,distpart: MultipartBody.Part,addpart: MultipartBody.Part,pinpart: MultipartBody.Part,profilepart: MultipartBody.Part) {
        profileRepository.getProfileData(part,useridpart,namepart,dobpart,genderpart,phonepart,emailpart,citypart,statepart,distpart,
            addpart,pinpart,profilepart, data)
    }



}