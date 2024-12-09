package com.alexp.shoppinglist.data

import android.app.Application
import android.view.animation.Transformation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.alexp.shopinglist.domain.ShopItem
import com.alexp.shopinglist.domain.ShopListRepository
import java.lang.RuntimeException
import javax.inject.Inject
import kotlin.random.Random

class ShopListRepositoryImpl @Inject constructor(
    private val shopListDao: ShopListDao,
    private val mapper : ShopListMapper,
) : ShopListRepository {

    override suspend fun addShopItem(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override suspend fun deleteShopItem(shopItem: ShopItem) {
        shopListDao.deleteShopItem(shopItem.id)

    }

    override fun getShopList(): LiveData<List<ShopItem>> = shopListDao.getShopList().map {
        mapper.mapListDbModelToListEnity(it)
    }

    override suspend fun editShopItem(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))


    }

    override suspend fun getShopItem(shopItemId: Int): ShopItem {

        val dbModel = shopListDao.getShopItem(shopItemId)

        return mapper.mapDbModelToEntity(dbModel)
    }




}