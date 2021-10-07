package co.vik.jobvo.screens.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class ProfileData(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val image: String,
    val gender: String,
    val profileUrl: String,
    val city: String,
    val mobile: String,
    val district: String,
    val userid: String,
    val dob: String,
    val name: String,
    val state: String,
    val email: String


)
