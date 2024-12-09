package com.alexp.shoppinglist.data

import com.alexp.shopinglist.domain.ShopItem
import javax.inject.Inject

class ShopListMapper @Inject constructor() {

    fun mapEntityToDbModel(shopItem: ShopItem) = ShopItemDbModel(
            id = shopItem.id,
            name = shopItem.name,
            count = shopItem.count,
            enabled = shopItem.enabled
            )

    fun mapDbModelToEntity(shopItemDbModel: ShopItemDbModel) = ShopItem(
        id = shopItemDbModel.id,
        name = shopItemDbModel.name,
        count = shopItemDbModel.count,
        enabled = shopItemDbModel.enabled
    )
    fun mapListDbModelToListEnity(list:List<ShopItemDbModel>) = list.map{
        mapDbModelToEntity(it)
    }

}