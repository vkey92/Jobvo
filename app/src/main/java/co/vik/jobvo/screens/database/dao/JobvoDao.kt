package co.vik.jobvo.screens.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import co.vik.jobvo.screens.database.entity.ProfileData

@Dao
interface JobvoDao {

    @Insert
    suspend fun insertProfile(profilemodel : ProfileData)

    @Update
    suspend fun updateProfile(profilemodel : ProfileData)

    @Delete
    suspend fun deleteProfile(profilemodel : ProfileData)

    @Query("SELECT * FROM profile")
    fun getProfile() : LiveData<List<ProfileData>>
}