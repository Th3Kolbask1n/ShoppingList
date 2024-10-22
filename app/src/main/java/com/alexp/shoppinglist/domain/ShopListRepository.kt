package com.alexp.shopinglist.domain

interface ShopListRepository {

    fun addShopItem(shopItem: ShopItem)
    fun deleteShopItem(shopItem: ShopItem)
    fun getShopList() : List<ShopItem>
    fun editShopItem(shopItem: ShopItem): ShopItem
    fun getShopItem(shopItemId: Int): ShopItem
}