package com.alexp.shoppinglist.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexp.shopinglist.domain.DeleteShopItemUseCase
import com.alexp.shopinglist.domain.EditShopItemUseCase
import com.alexp.shopinglist.domain.GetShopListUseCase
import com.alexp.shopinglist.domain.ShopItem
import com.alexp.shoppinglist.data.ShopListRepositoryImpl

class MainViewModel: ViewModel() {

    private val repository = ShopListRepositoryImpl
    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    val shopList = MutableLiveData<List<ShopItem>>()

    fun getShopList()
    {
        val list = getShopListUseCase.getShopList()
        shopList.postValue(list)
    }
    fun deleteShopItem(shopItem: ShopItem)
    {
        deleteShopItemUseCase.deleteShopItem(shopItem)
        getShopList()
    }
    fun changeEnableState(shopItem: ShopItem)
    {
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase.editShopItem(newItem)
        getShopList()
    }

}