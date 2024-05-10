package com.downappload.beanlogger.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.downappload.beanlogger.data.database.dao.LogDao
import com.downappload.beanlogger.data.database.entities.LogEntity

@Database(entities = [LogEntity::class], version = 3)
abstract class DatabaseAdapter : RoomDatabase() {

    abstract fun getLogDao(): LogDao

    companion object {

        private const val BD_NAME = "notifications"

        private var db: DatabaseAdapter? = null

        fun getInstance(context: Context): DatabaseAdapter {
            if (db == null) {
                db = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseAdapter::class.java, BD_NAME
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return db!!
        }

    }

}