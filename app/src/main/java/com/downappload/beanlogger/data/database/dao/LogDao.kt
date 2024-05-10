package com.downappload.beanlogger.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.downappload.beanlogger.data.database.entities.LogEntity

@Dao
interface LogDao {

    @Insert
    fun saveLog(logEntity: LogEntity)
}