package com.alexp.shoppinglist.di

import android.app.Application
import com.alexp.shopinglist.domain.ShopListRepository
import com.alexp.shoppinglist.data.AppDatabase
import com.alexp.shoppinglist.data.ShopListDao
import com.alexp.shoppinglist.data.ShopListRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindShopListRepository(impl: ShopListRepositoryImpl): ShopListRepository


    companion object {
        @ApplicationScope
        @Provides
        fun provideShopListDao(
            application: Application
        ): ShopListDao {
            return AppDatabase.getInstance(application).shopListDao()
        }
    }
}