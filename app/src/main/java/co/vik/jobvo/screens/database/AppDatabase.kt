package co.vik.jobvo.screens.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import co.vik.jobvo.pojo.ProfileModel
import co.vik.jobvo.screens.database.dao.JobvoDao
import co.vik.jobvo.screens.database.entity.ProfileData

@Database(entities = [ProfileData::class], version = 1)
//@TypeConverters(DataConvertor::class)
abstract class AppDatabase : RoomDatabase(){

    abstract fun jobvoDao() : JobvoDao
    companion object{
        @Volatile // it tells the updated value of instance to all threads
        private var INSTANCE : AppDatabase? = null

        fun getDataBase(context : Context) : AppDatabase{
            if(INSTANCE == null){
                // locking mechanism avoid to make multiple instance of database
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,AppDatabase::class.java,"jobvoDB").build()
                }

            }
            return INSTANCE!!
        }
    }
}