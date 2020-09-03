package com.askete.meditate.tools.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.askete.meditate.data.model.DataSnapshot
import com.askete.meditate.tools.database.dao.DataDao

@Database(entities = [DataSnapshot::class],version = 3)
abstract class AppDatabase: RoomDatabase() {
    abstract val dao: DataDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase {
            if (INSTANCE == null){
                synchronized(AppDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "myDB").build()
                }
            }
            return INSTANCE!!
        }
    }
}