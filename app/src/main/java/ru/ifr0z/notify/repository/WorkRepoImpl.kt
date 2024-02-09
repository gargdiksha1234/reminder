package com.workManagerr.notify.repository



import androidx.lifecycle.LiveData
import com.workManagerr.notify.db.WorkListDao
import com.workManagerr.notify.db.WorkListEntity
import com.workManagerr.notify.di.WorkListRepository
import javax.inject.Inject


class WorkRepoImpl  @Inject constructor(
    val workListDao: WorkListDao
) :WorkListRepository{

     override suspend fun addToList(workEntity: WorkListEntity) {
         workListDao.addToList(workEntity)
    }

    override suspend fun updateList(workEntity: WorkListEntity) {
         workListDao.updateData(workEntity)
    }

    override fun getUserList(): LiveData<List<WorkListEntity>> {
       return userData
    }

    override suspend fun deleteAllData() {
        workListDao.deleteAllData()
    }

    override suspend fun deleteData(work: WorkListEntity) {
       workListDao.deleteTask(work)
    }

    //    suspend fun getList(): List<WorkListEntity> {
//        return workListDao.getAllUser()
//    }
     val userData : LiveData<List<WorkListEntity>>
        get() = workListDao.getAllUser()


}