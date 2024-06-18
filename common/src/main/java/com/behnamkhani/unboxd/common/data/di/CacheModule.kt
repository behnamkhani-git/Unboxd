package com.behnamkhani.unboxd.common.data.di

import android.content.Context
import androidx.room.Room
import com.behnamkhani.unboxd.common.data.cache.Cache
import com.behnamkhani.unboxd.common.data.cache.UnboxdDatabase
import com.behnamkhani.unboxd.common.data.cache.RoomCache
import com.behnamkhani.unboxd.common.data.cache.daos.ProductsDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CacheModule {

    @Binds
    abstract fun bindCache(cache: RoomCache): Cache

    companion object {
        @Provides
        @Singleton
        fun provideDatabase(
            @ApplicationContext context: Context,
        ): UnboxdDatabase {
            return Room.databaseBuilder(
                context,
                UnboxdDatabase::class.java,
                "unboxd.db"
            )
                .build()
        }

//        @Provides
//        @Singleton
//        fun provideDatabaseInMemory(
//            @ApplicationContext context: Context,
//        ): UnboxdDatabase {
//            return Room.inMemoryDatabaseBuilder(
//                context,
//                UnboxdDatabase::class.java,
//            )
//
//                .build()
//        }

        @Provides
        fun provideProductsDao(
            petSaveDatabase: UnboxdDatabase,
        ): ProductsDao = petSaveDatabase.productsDao()
    }
}