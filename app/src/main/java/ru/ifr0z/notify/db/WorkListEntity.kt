package com.workManagerr.notify.db

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity()
data class WorkListEntity(
    @PrimaryKey(autoGenerate = true) val notificationId: Int,
    var isWorkComplete: Boolean = false,
    val title:String="",
    val desc:String="",
    val createdDate: Long = System.currentTimeMillis() // time in milliseconds
)
