package com.workManagerr.notify.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface WorkListDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToList(work: WorkListEntity)

    @Update
    suspend fun updateData(work: WorkListEntity)

    @Delete
    suspend fun deleteTask(workListEntity: WorkListEntity)

    @Query("DELETE FROM WorkListEntity")
    suspend fun deleteAllData()

    @Query("select * from WorkListEntity")
    fun getAllUser() : LiveData<List<WorkListEntity>>


}