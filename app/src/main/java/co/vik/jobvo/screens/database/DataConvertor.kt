package co.vik.jobvo.screens.database

import androidx.room.TypeConverter
import co.vik.jobvo.pojo.ProfileModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class DataConvertor {
    fun fromDateToLong(value : Date) : Long{
        return value.time
    }

    fun fromLongToDate(value : Long) : Date{
        return Date(value)
    }
}
