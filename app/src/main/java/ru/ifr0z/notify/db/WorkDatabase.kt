package com.workManagerr.notify.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities =  [WorkListEntity::class],
    version = 1,
    exportSchema = true
)
abstract class WorkDatabase : RoomDatabase(){

    abstract fun wishListDao(): WorkListDao

}
