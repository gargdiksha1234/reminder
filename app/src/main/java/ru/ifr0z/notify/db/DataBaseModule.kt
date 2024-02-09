package com.workManagerr.notify.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Provides
    @Singleton
    fun provideGuestWishListDataBase(@ApplicationContext context: Context): WorkDatabase {
        return Room.databaseBuilder(context.applicationContext,
            WorkDatabase::class.java, "Database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideWishListDao(eccoDatabase: WorkDatabase): WorkListDao {
        return eccoDatabase.wishListDao()
    }
}
