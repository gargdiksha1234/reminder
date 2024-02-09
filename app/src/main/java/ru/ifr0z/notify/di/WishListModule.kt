package com.workManagerr.notify.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.workManagerr.notify.db.WorkListDao
import com.workManagerr.notify.repository.WorkRepoImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WishListModule {




    @Provides
    @Singleton
    fun provideWishListRepository(
        wishListDao: WorkListDao
    ): WorkListRepository = WorkRepoImpl(wishListDao)

}
