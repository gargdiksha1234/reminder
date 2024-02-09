package com.workManagerr.notify.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch
import com.workManagerr.notify.db.WorkListEntity
import com.workManagerr.notify.repository.WorkRepoImpl
import javax.inject.Inject


@HiltViewModel
class WorkViewModel @Inject constructor(val workRepo: WorkRepoImpl):ViewModel(){



     lateinit var userList: LiveData< List<WorkListEntity>>


    fun addWork(work: WorkListEntity){

        viewModelScope.launch {
           workRepo.addToList(work)
        }
    }
 fun updateData(work: WorkListEntity){

        viewModelScope.launch {
            workRepo.updateList(work)
        }
    }
    fun deleteData(work: WorkListEntity){

        viewModelScope.launch {
            workRepo.deleteData(work)
        }
    }
    fun deleteAllData(){8

        viewModelScope.launch {
            workRepo.deleteAllData()
        }
    }
    val getData: LiveData<List<WorkListEntity>>
        get()=workRepo.userData

    fun getUserList(){
        viewModelScope.launch {
            userList=workRepo.getUserList()
        }
    }
}