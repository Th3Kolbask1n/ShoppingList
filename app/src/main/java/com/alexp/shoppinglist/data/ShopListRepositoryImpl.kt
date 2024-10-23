package com.alexp.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alexp.shopinglist.domain.ShopItem
import com.alexp.shopinglist.domain.ShopListRepository
import java.lang.RuntimeException

object ShopListRepositoryImpl  : ShopListRepository {

    private val shopList = mutableListOf<ShopItem>()
    private val shopListLD = MutableLiveData<List<ShopItem>>()
    private var autoIncrementId = 0
    init{
        for(i in 0 until 10)
        {
            val item = ShopItem("Name $i", i, true)
            addShopItem(item)
        }
    }
    override fun addShopItem(shopItem: ShopItem) {
        if(shopItem.id == ShopItem.UNDEFINED_ID)
            shopItem.id = autoIncrementId++

        shopList.add(shopItem)
        updateList()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateList()

    }

    override fun getShopList(): LiveData<List<ShopItem>> {
         return shopListLD
    }

    override fun editShopItem(shopItem: ShopItem) {
        val currentShopItem = getShopItem(shopItem.id)
        deleteShopItem(currentShopItem)
        addShopItem(shopItem)

    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopList.find { it.id ==  shopItemId} ?: throw RuntimeException("Element not found")


    }

    private fun updateList()
    {
        shopListLD.value = shopList.toList()
    }


}