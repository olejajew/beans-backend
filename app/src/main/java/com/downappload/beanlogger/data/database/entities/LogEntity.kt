package com.downappload.beanlogger.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val data: String = ""
)