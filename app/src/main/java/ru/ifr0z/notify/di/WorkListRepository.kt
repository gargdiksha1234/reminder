package com.workManagerr.notify.di


import androidx.lifecycle.LiveData
import com.workManagerr.notify.db.WorkListEntity

interface WorkListRepository {

    suspend fun addToList(wishListDBModel: WorkListEntity)
    suspend fun updateList(wishListDBModel: WorkListEntity)
    fun getUserList(): LiveData<List<WorkListEntity>>
    suspend fun deleteAllData()
    suspend fun deleteData(wishListDBModel: WorkListEntity)

}